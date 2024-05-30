package com.example.demo12.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo12.entity.Employees;
import com.example.demo12.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.demo12.service.EmployeesService;


@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);
    @Resource
    private EmployeesService EmployeesService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        //获取token
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
            //如果header没有带上token，则从参数中获取
            token = request.getParameter("token");
        }
        //开启认证
        if(StrUtil.isBlank(token)){
            throw new CustomException("无token,请先登录");
        }
        //验证token
        String EmployeeId;
        Employees Employees;
        try {
            //解析token
            EmployeeId = JWT.decode(token).getAudience().get(0);
            //根据textId获取text
            Employees = EmployeesService.FindById(Integer.parseInt(EmployeeId));
        } catch (Exception e) {
            String errMsg = "token解析失败,请重新登录";
            log.error(errMsg+"token="+token, e);
            throw new CustomException(errMsg);
        }
        if(Employees == null){
            throw new CustomException("不存在,请重新登录");
        }
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Employees.getPassword())).build();
            jwtVerifier.verify(token);//验证token
        }catch(JWTVerificationException e){
            throw new CustomException("token验证失败,请重新登录");
        }
        //log.info("token验证成功");
        return true;
    }

}
