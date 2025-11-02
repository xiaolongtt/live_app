package org.example.live.user.api.Vo.Req;

import com.example.livebankinterface.constants.PayChannelEnum;
import com.example.livebankinterface.constants.PaySourceEnum;
import lombok.Data;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 15:54
 * @注释
 */
@Data
public class PayProductReqVO {
    // 产品id
    private Integer productId;
    /**
     * 支付来源（直播间内，用户中心，广告跳转等），用于统计支付页面来源
        使用一个枚举类来管理
     * @see PaySourceEnum
     */
    private Integer paySource;
    /**
     * 支付渠道，支付宝/微信等
     * @see PayChannelEnum
     */
    private Integer payChannel;
}
