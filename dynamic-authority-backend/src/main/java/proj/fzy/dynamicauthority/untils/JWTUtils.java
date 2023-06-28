package proj.fzy.dynamicauthority.untils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import proj.fzy.dynamicauthority.config.JwtConfiguration;

import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    private final JwtConfiguration jwtConfiguration;

    public JWTUtils(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    public String generate(String username) {
        return JWT.create()
                .setIssuedAt(DateUtil.date())
                .setPayload("username", username)
                .setExpiresAt(DateUtil.offsetSecond(DateUtil.date(), jwtConfiguration.getDuration()))
                .setSigner(JWTSignerUtil.hs256(jwtConfiguration.getSecretKey().getBytes()))
                .sign();
    }

    public boolean verify(String token) {
        try {
            JWTValidator.of(token).validateAlgorithm(JWTSignerUtil.hs256("dynamic_authority".getBytes())).validateDate(DateUtil.date());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getUsername(String token) {
        return JWT.of(jwtConfiguration.getSecretKey()).getPayload("username").toString();
    }

    public String extraTokenFromHttpServletRequest(HttpServletRequest request) {
        String requestHeader = request.getHeader(jwtConfiguration.getHeader());
        String token = null;
        if (requestHeader != null && requestHeader.startsWith(jwtConfiguration.getPrefix())) {
            token = requestHeader.substring(jwtConfiguration.getPrefix().length());
        }
        return token;
    }

}
