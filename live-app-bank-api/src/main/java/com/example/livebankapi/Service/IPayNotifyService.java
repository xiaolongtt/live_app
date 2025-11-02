package com.example.livebankapi.Service;

import com.example.livebankapi.Vo.WxPayNotifyVO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 13:47
 * @注释
 */
public interface IPayNotifyService {

    /**
     * 用来处理微信支付回调，第三方返回的结果一般是一个json字符串，
     * 我们需要从这个字符串中解析出vo对象，
     * 并根据这些信息更新数据库中的订单状态。
     * @param paramsJson
     * @return
     */
    String notifyHandler(String paramsJson);
}
