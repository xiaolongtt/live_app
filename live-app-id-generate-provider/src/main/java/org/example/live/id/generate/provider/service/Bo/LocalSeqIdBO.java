package org.example.live.id.generate.provider.service.Bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/5 17:56
 * 有序id的BO对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalSeqIdBO {
    //mysql配置的id
    private Integer id;
    //对应分布式id的配置说明
    private String desc;
    //当前在本地内存的id值
    private AtomicLong currentValue;
    //本地内存记录id段的开始位置
    private Long currentStart;
    //本地内存记录id段的结束位置
    private Long nextThreshold;
    //步长
    private Integer step;

}