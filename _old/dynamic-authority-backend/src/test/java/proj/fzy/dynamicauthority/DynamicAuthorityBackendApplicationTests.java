package proj.fzy.dynamicauthority;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.apache.catalina.util.ToStringUtil;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import proj.fzy.dynamicauthority.enums.ResourcePermitAll;
import proj.fzy.dynamicauthority.untils.JWTUtils;

@SpringBootTest
class DynamicAuthorityBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
