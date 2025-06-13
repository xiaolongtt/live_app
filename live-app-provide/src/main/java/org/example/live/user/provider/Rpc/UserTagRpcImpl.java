package org.example.live.user.provider.Rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.user.constants.UserTagsEnum;
import org.example.live.user.interfaces.IUserTagRpc;
import org.example.live.user.provider.Service.IUserTagService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/6 20:11
 * @注释
 */
@DubboService
public class UserTagRpcImpl implements IUserTagRpc {
    @Resource
    private IUserTagService userTagService;
    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.setTag(userId,userTagsEnum);
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.cancelTag(userId,userTagsEnum);
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.containTag(userId,userTagsEnum);
    }
}
