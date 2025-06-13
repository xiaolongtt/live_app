package org.example.live.id.generate.provider.service.impl;

import jakarta.annotation.Resource;
import org.example.live.id.generate.provider.dao.mapper.IdBuilderMapper;
import org.example.live.id.generate.provider.dao.po.IdBuilderPO;
import org.example.live.id.generate.provider.service.Bo.LocalSeqIdBO;
import org.example.live.id.generate.provider.service.Bo.LocalUnSeqIdBO;
import org.example.live.id.generate.provider.service.IdBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/5 17:37
 * @注释
 */
@Service
public class IdBuilderServiceImpl implements IdBuilderService, InitializingBean {
    /**
     * InitializingBean 接口为bean提供了初始化方法的方式，要求实现afterPropertiesSet方法，
     * 在初始化bean的时候会执行该方法。可以用来初始化一些资源，如该类中的LocalSquIdMap
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IdBuilderServiceImpl.class);

    /**
     * 这个map在内存中维护了有序id的对应关系
     */
    private static final Map<Integer, LocalSeqIdBO> LocalSquIdMap = new ConcurrentHashMap<>();
    /**
     * 这个map在内存中维护了无序id的对应关系
     */
    private static final Map<Integer, LocalUnSeqIdBO> LocalUnSeqIdMap = new ConcurrentHashMap<>();

    /**
     * 当前值达到阈值的80%时，触发更新的异步任务
     */
    private static final float UPDATE_RATE = 0.8f;

    /**
     * id的类型，1代表有序id，0代表无序id
     */
    private static final int ID_TYPE=1;

    /**
     * 创建一个线程池用来进行异步任务的执行
     * 核心线程数为8，最大线程数为16，空闲线程数为60秒，队列大小为1000
     */
    private static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("id-generate-thread");
                    return thread;
                }
    });
    /**
     * 信号量，用来控制并发数
     */
    private static Map<Integer,Semaphore> semaphoreMap=new ConcurrentHashMap<>();

    @Resource
    private IdBuilderMapper idBuilderMapper;

    /**
     * 生成无序id
     * @param code
     * @return
     */
    @Override
    public Long increaseUnSeqId(Integer code) {
        if(code==null){
            LOGGER.error("[increaseUnSeqId]中的code为空");
            return null;
        }
        LocalUnSeqIdBO localUnSeqIdBO = LocalUnSeqIdMap.get(code);
        if(localUnSeqIdBO==null){
            LOGGER.error("[increaseUnSeqId]中的code输入有问题，localSeqIdBO为null");
            return null;
        }
        Long resultID = localUnSeqIdBO.getIdQueue().poll();
        if(resultID==null){
            LOGGER.error("[increaseUnSeqId]中的resultID为null");
            return null;
        }
        refreshLocalUnSeqId(localUnSeqIdBO);
        return resultID;
    }

    /**
     * 生成有序id
     * @param code 对应数据库中的id字段，根据这个id字段去查询对应的配置信息和id生成策略
     * @return
     */
    @Override
    public Long increaseSeqId(Integer code) {
        if(code==null){
            LOGGER.error("[increaseSeqId]中的code为空");
            return null;
        }
        LocalSeqIdBO localSeqIdBO = LocalSquIdMap.get(code);
        if(localSeqIdBO==null){
            LOGGER.error("[increaseSeqId]中的code输入有问题，localSeqIdBO为null");
            return null;
        }
        refreshLocalSeqId(localSeqIdBO);
        //getAndIncrement()获取当前的值，然后将其递增,返回的是递增前的值（而且是原子操作，多线程安全）
        long num = localSeqIdBO.getCurrentValue().getAndIncrement();
        if(num>localSeqIdBO.getNextThreshold()){
            LOGGER.error("分配的id已经达到上限");
            return null;
        }
        return num;
    }

    /**
     * 有序id的更新策略
     * 当分配的id数量达到阈值时，需要提前占用相应的id段并且更新数据库
     * @param localSeqIdBO
     */
    private void refreshLocalSeqId(LocalSeqIdBO localSeqIdBO){
        long step=localSeqIdBO.getStep();
        //如果当前分配的id数量已经大于了更新的阈值，就需要更新了
        if(localSeqIdBO.getCurrentValue().get()-localSeqIdBO.getCurrentStart()>step*UPDATE_RATE){
            Semaphore semaphore = semaphoreMap.get(localSeqIdBO.getId());
            if(semaphore==null){
                LOGGER.error("[refreshLocalSeqId]中的semaphore为null");
                return;
            }
            boolean acquire = semaphore.tryAcquire();
            if(acquire){
                //异步进行更新
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            IdBuilderPO idBuilderPO = idBuilderMapper.selectById(localSeqIdBO.getId());
                            updateRetry(idBuilderPO);
                        }catch (Exception e){
                            LOGGER.error("[refreshLocalSeqId]中的e为{}",e);
                        }finally {
                            //释放信号量
                            semaphore.release();
                        }
                    }
                });
            }
        }
    }

    /**
     * 无序id的刷新策略
     * @param localUnSeqIdBO
     */

    private void refreshLocalUnSeqId(LocalUnSeqIdBO localUnSeqIdBO) {
        int step = localUnSeqIdBO.getStep();
        if(localUnSeqIdBO.getIdQueue().size()<step*(1-UPDATE_RATE)){
            Semaphore semaphore = semaphoreMap.get(localUnSeqIdBO.getId());
            if(semaphore==null){
                LOGGER.error("[refreshLocalUnSeqId]中的semaphore为null");
                return;
            }
            boolean acquire = semaphore.tryAcquire();
            if(acquire){
                //异步进行更新
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdBuilderPO idBuilderPO = idBuilderMapper.selectById(localUnSeqIdBO.getId());
                            updateRetry(idBuilderPO);
                        }catch (Exception e){
                            LOGGER.error("[refreshLocalUnSeqId]中的e为{}",e);
                        }finally {
                            //释放信号量
                            semaphore.release();
                        }
                    }
                });
            }
        }
    }

    /**
     * spring初始化bean后执行的方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdBuilderPO> idBuilderPOS = idBuilderMapper.SelectAll();
        idBuilderPOS.forEach(idBuilderPO -> {
            LOGGER.info("初始化MAP");
            updateRetry(idBuilderPO);
            semaphoreMap.put(idBuilderPO.getId(),new Semaphore(1));
        });
    }

    /**
     * 更新数据库中的id的配置信息，占用相应的id段
     * 1.先查询数据库中id对应的配置信息
     * 2.更新数据库中的id的配置信息，占用相应的id段
     * 3.更新成功了，将id的配置信息放入map中
     * 4.更新失败了，反复尝试3次
     * 5.如果3次都失败了，就抛出一个异常
     * @param idBuilderPO
     */
    private void updateRetry(IdBuilderPO idBuilderPO){
        long currentStart = idBuilderPO.getCurrentStart();
        long nextThreshold = idBuilderPO.getNextThreshold();
        long currentValue = currentStart;
        int updateResult = idBuilderMapper.updateIdAndVersion(idBuilderPO.getId(), idBuilderPO.getVersion());
        //更新成功了
        if(updateResult!=0) {
            if(idBuilderPO.getIsSeq()==ID_TYPE){
                LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
                AtomicLong Atom_currentValue = new AtomicLong(currentValue);
                localSeqIdBO.setCurrentStart(currentStart);
                localSeqIdBO.setNextThreshold(nextThreshold);
                localSeqIdBO.setCurrentValue(Atom_currentValue);
                localSeqIdBO.setId(idBuilderPO.getId());
                LocalSquIdMap.put(idBuilderPO.getId(), localSeqIdBO);
                return;
            }else{
                LocalUnSeqIdBO localUnSeqIdBO = new LocalUnSeqIdBO();
                localUnSeqIdBO.setCurrentStart(currentStart);
                localUnSeqIdBO.setNextThreshold(nextThreshold);
                localUnSeqIdBO.setId(idBuilderPO.getId());
                long begin = currentStart;
                long end = nextThreshold;
                localUnSeqIdBO.setRandomIdInQueue(begin,end);
                LocalUnSeqIdMap.put(idBuilderPO.getId(), localUnSeqIdBO);
                return;
            }

        }
        //更新失败了，反复尝试3次
        for(int j=0;j<3;j++){
            idBuilderPO = idBuilderMapper.selectById(idBuilderPO.getId());
            updateResult = idBuilderMapper.updateIdAndVersion(idBuilderPO.getId(), idBuilderPO.getVersion());
            if(updateResult>0) {
                if(idBuilderPO.getIsSeq()==ID_TYPE){
                    LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
                    AtomicLong Atom_currentValue = new AtomicLong(idBuilderPO.getCurrentStart());
                    localSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
                    localSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
                    localSeqIdBO.setCurrentValue(Atom_currentValue);
                    localSeqIdBO.setId(idBuilderPO.getId());
                    LocalSquIdMap.put(idBuilderPO.getId(), localSeqIdBO);
                    return;
                }else{
                    LocalUnSeqIdBO localUnSeqIdBO = new LocalUnSeqIdBO();
                    localUnSeqIdBO.setCurrentStart(idBuilderPO.getCurrentStart());
                    localUnSeqIdBO.setNextThreshold(idBuilderPO.getNextThreshold());
                    localUnSeqIdBO.setId(idBuilderPO.getId());
                    long begin = idBuilderPO.getCurrentStart();
                    long end = idBuilderPO.getNextThreshold();
                    localUnSeqIdBO.setRandomIdInQueue(begin,end);
                    LocalUnSeqIdMap.put(idBuilderPO.getId(), localUnSeqIdBO);
                    return;
                }
            }
        }
        throw new RuntimeException("更新失败");
    }
}
