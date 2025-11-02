package org.example.live.user.api.Service;


import org.example.live.user.api.Vo.Req.PayProductReqVO;
import org.example.live.user.api.Vo.Resp.PayProductRespVO;
import org.example.live.user.api.Vo.Resp.PayProductVO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/22 21:08
 * @注释
 */
public interface IBankService {
    /**
     * 查询相关的产品列表信息
     *
     * @param type
     * @return
     */
    PayProductVO products(Integer type);

    /**
     * 支付相关的产品
     *
     * @param payProductReqVO
     * @return
     */
    PayProductRespVO payProduct(PayProductReqVO payProductReqVO);
}
