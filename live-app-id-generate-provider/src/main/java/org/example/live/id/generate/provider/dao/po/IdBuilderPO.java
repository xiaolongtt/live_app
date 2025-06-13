package org.example.live.id.generate.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author idea
 * @Date: Created in 19:59 2023/5/23
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_id_builder_config")
public class IdBuilderPO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * id备注描述
     */
    private String remark;

    /**
     * 初始化值
     */
    private long initNum;

    /**
     * 步长
     */
    private int step;

    /**
     * 是否是有序的id
     */
    private int isSeq;

    /**
     * 当前id所在阶段的开始值
     */
    private long currentStart;

    /**
     * 当前id所在阶段的阈值
     */
    private long nextThreshold;

    /**
     * 业务代码前缀
     */
    private String idPrefix;

    /**
     * 乐观锁版本号
     */
    private int version;

    private Date createTime;

    private Date updateTime;
}