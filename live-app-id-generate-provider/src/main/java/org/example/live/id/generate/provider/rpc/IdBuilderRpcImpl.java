package org.example.live.id.generate.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.id.generate.interfaces.IdBuilderRpc;
import org.example.live.id.generate.provider.service.IdBuilderService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/5 17:35
 * @注释
 */
@DubboService
public class IdBuilderRpcImpl implements IdBuilderRpc {

    @Resource
    private IdBuilderService idBuilderService;

    @Override
    public Long increaseSeqId(Integer code) {
        return idBuilderService.increaseSeqId(code);
    }

    @Override
    public Long increaseUnSeqId(Integer code) {
        return idBuilderService.increaseUnSeqId(code);
    }

}
