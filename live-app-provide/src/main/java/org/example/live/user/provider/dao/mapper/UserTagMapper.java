package org.example.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.example.live.user.provider.dao.po.UserTagPO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/6 20:12
 * @注释
 */
@Mapper
public interface UserTagMapper extends BaseMapper<UserTagPO> {
    /**
     * 给用户设置标签，如果用户没有标签，则插入，只要第一次操作会成功
     * @param userId
     * @param fieldName
     * @param tag
     * @return
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} | #{tag} where user_id = #{userId} and ${fieldName} & #{tag} = 0")
    int setTag(Long userId, String fieldName, long tag);

    /**
     * 取消用户标签，如果用户没有标签，则不操作
     * @param userId
     * @param fieldName
     * @param tag
     * @return
     */
    @Update("update t_user_tag set ${fieldName} = ${fieldName} & ~#{tag} where user_id = #{userId} and ${fieldName} & #{tag} = #{tag} ")
    int cancelTag(Long userId, String fieldName, long tag);
}
