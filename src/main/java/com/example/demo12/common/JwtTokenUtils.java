package com.example.demo12.common;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo12.entity.Employees;
import com.example.demo12.service.EmployeesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenUtils {
    private static EmployeesService staticEmployeeService;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);
    @Resource
    private EmployeesService EmployeesService;

    @PostConstruct
    public void setUserService() {
        staticEmployeeService = EmployeesService;
    }
//生成token
    public static String genToken(String userId, String password) {
        return JWT.create().withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(password));
    }
//获取当前用户
    public static Employees getCurrentUser() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                log.error("获取token失败,token{}", token);
                return null;
            }
            String EmployeeId = JWT.decode(token).getAudience().get(0);
            return staticEmployeeService.FindById(Integer.valueOf(EmployeeId));
        } catch (Exception e) {
            log.error("获取当前用户失败,token{}", token);
            return null;
        }
    }
}
