package stu.fzy.dynamicauthorization;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import stu.fzy.dynamicauthorization.annotation.PermitAll;
import stu.fzy.dynamicauthorization.utils.JWTUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@SpringBootTest
class DynamicAuthorizationBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
