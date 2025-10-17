package com.example.livegiftprovider.Rpc;

import com.example.livegiftinterface.Dto.GiftConfigDTO;
import com.example.livegiftinterface.Interface.IGiftConfigRpc;
import com.example.livegiftprovider.Service.IGiftConfigService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/17 18:52
 * @注释
 */
@DubboService
public class GiftConfigRpcImpl implements IGiftConfigRpc {

    @Resource
    private IGiftConfigService iGiftConfigService;
    @Override
    public GiftConfigDTO getByGiftId(Integer giftId) {
        return iGiftConfigService.getByGiftId(giftId);
    }

    @Override
    public List<GiftConfigDTO> queryGiftList() {
        return iGiftConfigService.queryGiftList();
    }

    @Override
    public void insertOne(GiftConfigDTO giftConfigDTO) {
        iGiftConfigService.insertOne(giftConfigDTO);
    }

    @Override
    public void updateOne(GiftConfigDTO giftConfigDTO) {
        iGiftConfigService.updateOne(giftConfigDTO);
    }
}
