package org.example.live.user.api.Service.Impl;

import com.example.liveappaccountinterface.IAccountTokenRPC;
import com.example.webstarter.Error.ErrorAssert;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.common.interfaces.Vo.WebResponseVO;
import org.example.live.msg.constants.MsgSendResultEnum;
import org.example.live.msg.dto.MsgCheckDTO;
import org.example.live.msg.interfaces.IMsgRpc;
import org.example.live.user.api.Error.ApiErrorEnum;
import org.example.live.user.api.Service.IUserLoginService;
import org.example.live.user.dto.UserLoginDTO;
import org.example.live.user.interfaces.IUserPhoneRpc;
import org.springframework.beans.BeanUtils;

import java.util.regex.Pattern;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 9:56
 * @注释
 */
public class IUserLoginServiceImpl implements IUserLoginService {

    //手机号的正则表达式
    private final String PHONE_REGEX = "/^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[189]))\\d{8}$/";

    @DubboReference
    private IMsgRpc iMsgRpc;

    @DubboReference
    private IUserPhoneRpc iUserPhoneRpc;
    @DubboReference
    private IAccountTokenRPC iAccountTokenRpc;
    @Override
    public WebResponseVO sendLoginCode(String phone) {
        //先校验手机号格式正确不
        ErrorAssert.isNotNull(phone, ApiErrorEnum.PHONE_NOT_BLANK);
        //校验手机号格式是否正确
        ErrorAssert.isTure(Pattern.matches(PHONE_REGEX, phone), ApiErrorEnum.PHONE_IN_VALID);
        //生成验证码,并且发送给用户
        MsgSendResultEnum msgSendResultEnum = iMsgRpc.sendMessage(phone);
        if(msgSendResultEnum!=MsgSendResultEnum.SEND_SUCCESS){
            return WebResponseVO.errorParam(msgSendResultEnum.getDesc());
        }
        return WebResponseVO.success();
    }

    @Override
    public WebResponseVO login(String phone, Integer code, HttpServletResponse response) {
        //用户登录，校验手机号格式是否正确
        ErrorAssert.isNotNull(phone, ApiErrorEnum.PHONE_NOT_BLANK);
        ErrorAssert.isTure(Pattern.matches(PHONE_REGEX, phone), ApiErrorEnum.PHONE_IN_VALID);
        //校验验证码是否正确
        MsgCheckDTO msgCheckDTO = iMsgRpc.checkLoginCode(phone, code);
        if(!msgCheckDTO.isCheckStatus()) {
            //校验失败，设置登录状态
            return WebResponseVO.bizError(msgCheckDTO.getDesc());
        }
        // 封装token到cookie返回
        //用户验证码校验成功，登录用户
        UserLoginDTO userLoginDTO = iUserPhoneRpc.login(phone);
        //创建token
        String loginToken = iAccountTokenRpc.createAndSaveLoginToken(userLoginDTO.getUserId());
        Cookie cookie = new Cookie("live_token", loginToken);
        // 设置在哪个域名的访问下，才携带此cookie进行访问
        // https://app.qiyu.live.com//
        // https://api.qiyu.live.com//
        // 取公共部分的顶级域名，如果在hosts中自定义域名有跨域限制无法解决的话就注释掉setDomain和setPath
        // cookie.setDomain("qiyu.live.com");
        // 这里我们不设置域名，就设置为localhost
        cookie.setDomain("localhost");
        // 域名下的所有路径
        cookie.setPath("/");
        // 设置cookie过期时间，单位为秒，设置为token的过期时间，30天
        cookie.setMaxAge(30 * 24 * 3600);
        // 加上它，不然浏览器不会记录cookie
        // response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addCookie(cookie);
        return WebResponseVO.success(userLoginDTO);
    }
}
