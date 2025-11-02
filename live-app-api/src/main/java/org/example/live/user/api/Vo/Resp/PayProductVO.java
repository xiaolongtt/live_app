package org.example.live.user.api.Vo.Resp;

import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/22 21:23
 * @注释
 */
@Data
public class PayProductVO {
    /**
     * 当前余额
     */
    private Integer currentBalance;

    /**
     * 一系列的付费产品
     */
    private List<PayProductItemVO> payProductItemVOList;
}
