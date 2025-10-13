package org.example.living.provider.Config;

import jakarta.annotation.Resource;
import org.example.living.constants.LivingRoomRedisKey;
import org.example.living.constants.LivingRoomTypeEnum;
import org.example.living.dto.LivingRoomRespDTO;
import org.example.living.provider.Service.ILivingRoomService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 22:15
 * @注释 实现一个定时任务，定时刷新直播房间列表储存到redis中，默认每1000ms刷新一次，使用一个分布式锁，保证只有一个线程进行刷新
 */
public class RefreshLivingRoomListJob implements InitializingBean {

    private static final String LIVING_ROOM_LOCK_KEY="living_room_lock";

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private ILivingRoomService livingRoomService;


    /**
     * 创建了一个线程池，用于定时刷新直播房间列表，默认每1000ms刷新一次
     */
    @Resource
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =new ScheduledThreadPoolExecutor(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        //创建一个定时任务
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new RefreshCacheListJob(),3000,1000, TimeUnit.MILLISECONDS);
    }
    public void refreshCache(Integer type){
        //先调用服务层方法，获取到直播间列表
        List<LivingRoomRespDTO> livingRoomRespDTOS = livingRoomService.selectAllFromDb(type);
        if(livingRoomRespDTOS.isEmpty()){
            redisTemplate.delete(LivingRoomRedisKey.LIVING_ROOM_LIST_KEY);
            return;
        }
        String tempKey=LivingRoomRedisKey.LIVING_ROOM_LIST_KEY+"_temp";
        //将数据一个一个塞入redis中
        for (LivingRoomRespDTO livingRoomRespDTO : livingRoomRespDTOS) {
            redisTemplate.opsForList().rightPush(tempKey,livingRoomRespDTO);
        }
        //正在访问的list集合，del -> leftPush
        //直接修改重命名这个list，不要直接对原先的list进行修改，减少阻塞影响 cow
        redisTemplate.rename(tempKey,LivingRoomRedisKey.LIVING_ROOM_LIST_KEY);
        redisTemplate.delete(tempKey);
    }

    class RefreshCacheListJob implements Runnable{
        @Override
        public void run() {
            //使用一个分布式锁，保证只有一个线程进行定时任务刷新缓存
            Boolean lock = redisTemplate.opsForValue().setIfAbsent(LIVING_ROOM_LOCK_KEY, "1", 1, TimeUnit.SECONDS);
            if(lock){
                //刷新两种类型的直播间列表
                refreshCache(LivingRoomTypeEnum.DEFAULT_LIVING_ROOM.getCode());
                refreshCache(LivingRoomTypeEnum.PK_LIVING_ROOM.getCode());
            }
        }
    }

}
