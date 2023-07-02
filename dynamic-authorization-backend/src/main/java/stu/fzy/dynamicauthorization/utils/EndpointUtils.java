package stu.fzy.dynamicauthorization.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import stu.fzy.dynamicauthorization.annotation.PermitAll;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

@Component
public class EndpointUtils {
    private static final String HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME = "mvcHandlerMappingIntrospector";

    private final ApplicationContext applicationContext;
    private volatile Map<String, Set<String>> appEndpoints;
    private volatile Map<String, Set<String>> permitAllEndpoints;
    private volatile List<RequestMatcherEntry<String>> matcherEntries;

    public EndpointUtils(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Map<String, Set<String>> getAppEndpoints() {
        if (this.appEndpoints == null) {
            synchronized (this) {
                if (this.appEndpoints == null) {
                    Map<String, Set<String>> endpoints = new HashMap<>();
                    Set<RequestMappingInfo> requestMappingInfos = applicationContext
                            .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class)
                            .getHandlerMethods()
                            .keySet();
                    for (RequestMappingInfo requestMappingInfo : requestMappingInfos) {
                        if (!requestMappingInfo.getMethodsCondition().isEmpty()) {
                            Set<String> patterns = null;
                            Set<String> pathPatterns = null;
                            // handle patterns
                            if (requestMappingInfo.getPatternsCondition() != null &&
                                    !requestMappingInfo.getPatternsCondition().isEmpty()) {
                                patterns = requestMappingInfo.getPatternsCondition().getPatterns();
                            }
                            // handle path patterns
                            if (requestMappingInfo.getPathPatternsCondition() != null &&
                                    !requestMappingInfo.getPathPatternsCondition().isEmpty()) {
                                pathPatterns = requestMappingInfo.getPathPatternsCondition().getPatternValues();
                            }
                            // handle methods
                            Set<RequestMethod> methods = requestMappingInfo.getMethodsCondition().getMethods();
                            for (RequestMethod method : methods) {
                                String httpMethod = method.name();
                                if (!endpoints.containsKey(httpMethod)) {
                                    endpoints.put(httpMethod, new HashSet<>());
                                }
                                Set<String> paths = endpoints.get(httpMethod);
                                if (patterns != null) {
                                    paths.addAll(patterns);
                                }
                                if (pathPatterns != null) {
                                    paths.addAll(pathPatterns);
                                }
                            }
                        }
                    }
                    this.appEndpoints = endpoints;
                }
            }
        }
        return this.appEndpoints;
    }

    public Map<String, Set<String>> differenceFromLeft(Map<String, Set<String>> m1, Map<String, Set<String>> m2) {
        Map<String, Set<String>> differenceMapping = new HashMap<>();
        m1.forEach((method, paths) -> {
            paths.forEach(path -> {
                if (m2 == null || m2.get(method) == null || !m2.get(method).contains(path)) {
                    differenceMapping.putIfAbsent(method, new HashSet<>());
                    differenceMapping.get(method).add(path);
                }
            });
        });
        return differenceMapping;
    }

    public Map<String, Set<String>> getEndpointsWithPermitAll() {
        if (this.permitAllEndpoints == null) {
            synchronized (this) {
                if (this.permitAllEndpoints == null) {
                    Map<String, Set<String>> endpoints = new HashMap<>();
                    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);
                    for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
                        Object bean = entry.getValue();
                        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
                        RequestMapping requestMapping = bean.getClass().getAnnotation(RequestMapping.class);
                        String basePath = "";
                        if (requestMapping != null && requestMapping.value().length > 0) {
                            basePath = requestMapping.value()[0];
                        }
                        BiFunction<String[], String, String[]> concatPrefix = (ss, prefix) -> {
                            for (int i = 0; i < ss.length; i++) {
                                ss[i] = prefix + ss[i];
                            }
                            return ss;
                        };
                        for (Method method : methods) {
                            PermitAll permitAllAnnotation = method.getAnnotation(PermitAll.class);
                            if (permitAllAnnotation != null) {
                                for (Annotation annotation : method.getAnnotations()) {
                                    if (annotation.annotationType().equals(PostMapping.class)) {
                                        endpoints.putIfAbsent("POST", new HashSet<>());
                                        PostMapping postMapping = (PostMapping) annotation;
                                        endpoints.get("POST").addAll(Set.of(concatPrefix.apply(postMapping.value(), basePath)));
                                    }
                                    if (annotation.annotationType().equals(DeleteMapping.class)) {
                                        endpoints.putIfAbsent("DELETE", new HashSet<>());
                                        DeleteMapping deleteMapping = (DeleteMapping) annotation;
                                        endpoints.get("DELETE").addAll(Set.of(concatPrefix.apply(deleteMapping.value(), basePath)));
                                    }
                                    if (annotation.annotationType().equals(PutMapping.class)) {
                                        endpoints.putIfAbsent("PUT", new HashSet<>());
                                        PutMapping putMapping = (PutMapping) annotation;
                                        endpoints.get("PUT").addAll(Set.of(concatPrefix.apply(putMapping.value(), basePath)));
                                    }
                                    if (annotation.annotationType().equals(PatchMapping.class)) {
                                        endpoints.putIfAbsent("PATCH", new HashSet<>());
                                        PatchMapping patchMapping = (PatchMapping) annotation;
                                        endpoints.get("PATCH").addAll(Set.of(concatPrefix.apply(patchMapping.value(), basePath)));
                                    }
                                    if (annotation.annotationType().equals(GetMapping.class)) {
                                        endpoints.putIfAbsent("GET", new HashSet<>());
                                        GetMapping getMapping = (GetMapping) annotation;
                                        endpoints.get("GET").addAll(Set.of(concatPrefix.apply(getMapping.value(), basePath)));
                                    }
                                }
                            }
                        }
                    }
                    this.permitAllEndpoints = endpoints;
                }
            }
        }
        return this.permitAllEndpoints;
    }

    public List<RequestMatcherEntry<String>> getMatcherEntries() {
        if (this.matcherEntries == null) {
            synchronized (this) {
                if (this.matcherEntries == null) {
                    Map<String, Set<String>> endpoints = getAppEndpoints();
                    List<RequestMatcherEntry<String>> requestMatcherEntries = new ArrayList<>();
                    HandlerMappingIntrospector introspector =
                            applicationContext.getBean(HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME, HandlerMappingIntrospector.class);
                    for (Map.Entry<String, Set<String>> entry : endpoints.entrySet()) {
                        for (String path : entry.getValue()) {
                            requestMatcherEntries.add(new RequestMatcherEntry<>(new MvcRequestMatcher(introspector, path), path));
                        }
                    }
                    this.matcherEntries = requestMatcherEntries;
                }
            }
        }
        return this.matcherEntries;
    }

    public String getRequestPattern(HttpServletRequest request) {
        List<RequestMatcherEntry<String>> requestMatcherEntries = getMatcherEntries();
        for (RequestMatcherEntry<String> requestMatcherEntry : requestMatcherEntries) {
            if (requestMatcherEntry.getRequestMatcher().matches(request)) {
                return requestMatcherEntry.getEntry();
            }
        }
        return "";
    }

    public boolean isPermitAll(HttpServletRequest request) {
        Map<String, Set<String>> permitAllEndpoints = getEndpointsWithPermitAll();
        String method = request.getMethod();
        String requestPattern = getRequestPattern(request);
        return permitAllEndpoints.containsKey(method) && permitAllEndpoints.get(method).contains(requestPattern);
    }

}
