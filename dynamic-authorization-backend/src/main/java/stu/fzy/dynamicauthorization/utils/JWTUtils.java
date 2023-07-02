package stu.fzy.dynamicauthorization.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import stu.fzy.dynamicauthorization.config.JWTConfigInfo;

@Component
public class JWTUtils {

    private final JWTConfigInfo jwtConfigInfo;

    public JWTUtils(JWTConfigInfo jwtConfigInfo) {
        this.jwtConfigInfo = jwtConfigInfo;
    }

    public String generate(String username) {
        return JWT.create()
                .setIssuedAt(DateUtil.date())
                .setPayload("username", username)
                .setExpiresAt(DateUtil.offsetSecond(DateUtil.date(), jwtConfigInfo.getDuration()))
                .setSigner(JWTSignerUtil.hs256(jwtConfigInfo.getSecretKey().getBytes()))
                .sign();
    }

    public boolean verify(String token) {
        try {
            JWTValidator
                    .of(token)
                    .validateAlgorithm(JWTSignerUtil.hs256("dynamic_authority".getBytes()))
                    .validateDate(DateUtil.date());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getUsername(String token) {
        return (String) JWTUtil
                .parseToken(token)
                .setKey(jwtConfigInfo.getSecretKey().getBytes())
                .getPayload("username");
    }

    public String extraTokenFromHttpServletRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader(jwtConfigInfo.getHeader());
        String token = null;
        if (requestHeader != null && requestHeader.startsWith(jwtConfigInfo.getPrefix())) {
            token = requestHeader.substring(jwtConfigInfo.getPrefix().length());
        }
        return token;
    }

}
