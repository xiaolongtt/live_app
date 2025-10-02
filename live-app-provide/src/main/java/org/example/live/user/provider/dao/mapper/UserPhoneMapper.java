package org.example.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.live.user.provider.dao.po.UserPO;
import org.example.live.user.provider.dao.po.UserPhonePO;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/9/6 14:52
 * @注释
 */
public interface UserPhoneMapper extends BaseMapper<UserPhonePO> {
    @Select("select * from t_user_phone where phone = #{phone} and status = 1 limit 1")
    UserPhonePO selectByUserPhone(String phone);

    @Select("select * from t_user_phone where user_id = #{userId} and status = 1")
    List<UserPhonePO> selectByUserId(Long userId);
}
