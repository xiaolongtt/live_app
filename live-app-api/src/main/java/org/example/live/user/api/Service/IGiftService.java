package org.example.live.user.api.Service;

import org.example.live.user.api.Vo.Req.GiftReqVO;
import org.example.live.user.api.Vo.Resp.GiftConfigVO;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 11:32
 * @注释
 */
public interface IGiftService {
    /**
     * 展示礼物列表
     *
     * @return
     */
    List<GiftConfigVO> listGift();

    /**
     * 送礼
     *
     * @param giftReqVO
     * @return
     */
    boolean sendGift(GiftReqVO giftReqVO);
}
