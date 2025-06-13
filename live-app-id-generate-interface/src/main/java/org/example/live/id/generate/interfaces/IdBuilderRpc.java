package org.example.live.id.generate.interfaces;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/5 17:33
 * @注释 分布式id生成器的dubbo接口
 */
public interface IdBuilderRpc {
    /**
     * 根据本地步长度来生成唯一id(区间性递增)
     *
     * @return
     */
    Long increaseSeqId(Integer code);

    /**
     * 生成的是非连续性id
     *
     * @param code
     * @return
     */
    Long increaseUnSeqId(Integer code);

}
