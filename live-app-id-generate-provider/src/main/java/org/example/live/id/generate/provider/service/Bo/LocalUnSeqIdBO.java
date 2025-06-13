package org.example.live.id.generate.provider.service.Bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/5 17:37
 * @注释 无序id的BO对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalUnSeqIdBO {

    //mysql配置的id
    private Integer id;
    //对应分布式id的配置说明
    private String desc;
    //链表记录id值
    private ConcurrentLinkedQueue<Long> idQueue;
    //本地内存记录id段的开始位置
    private Long currentStart;
    //本地内存记录id段的结束位置
    private Long nextThreshold;
    //步长
    private Integer step;

    /**
     * 把打乱的id放入对列中
     * @param begin
     * @param end
     */
    public void setRandomIdInQueue(long begin,long end) {
        List<Long> idList = new LinkedList<>();
        for (long j = begin; j < end; j++) {
            idList.add(j);
        }
        //把队列的元素进行打乱
        Collections.shuffle(idList);
        ConcurrentLinkedQueue idQueue = new ConcurrentLinkedQueue();
        idQueue.addAll(idList);
        this.setIdQueue(idQueue);
    }
}