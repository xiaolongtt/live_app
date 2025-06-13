package org.example.live.user.interfaces;

import org.example.live.user.constants.UserTagsEnum;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/6 20:08
 * @注释 用户标签接口层
 */
public interface IUserTagRpc {
    /**
     * 设置标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    /**
     * 取消标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean cancelTag(Long userId,UserTagsEnum userTagsEnum);

    /**
     * 是否包含某个标签
     *
     * @param userId
     * @param userTagsEnum
     * @return
     */
    boolean containTag(Long userId,UserTagsEnum userTagsEnum);
}
