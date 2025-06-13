package org.example.live.id.generate.provider.service;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/5 17:36
 * @注释
 */
public interface IdBuilderService {
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
