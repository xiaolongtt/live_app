package org.example.live.id.generate.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.live.id.generate.provider.dao.po.IdBuilderPO;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/5 17:56
 * @注释
 */
@Mapper
public interface IdBuilderMapper extends BaseMapper<IdBuilderPO> {

    @Update("update live_app_common.t_id_generate_config set current_start = next_threshold,next_threshold=current_start+step,version=version+1 where id=#{code}")
    Integer updateNewVersion(@Param("code") Integer code);

    @Update("update live_app_common.t_id_generate_config set next_threshold=next_threshold+step,current_start=current_start+step,version=version+1 where id=? and version=?")
    int updateIdAndVersion(@Param("id") int id, @Param("version") int version);
    @Update("UPDATE live_app_common.t_id_generate_config set next_threshold=#{nextThreshold},current_start=#{currentStart},version=version+1 where id=#{id} and version=#{version}")
    Integer updateCurrentThreshold(@Param("nextThreshold") long nextThreshold, @Param("currentStart") long currentStart, @Param("id") int id, @Param("version") int version);

    @Select("select * from live_app_common.t_id_generate_config")
    List<IdBuilderPO> SelectAll();
}
