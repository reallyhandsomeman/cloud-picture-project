package com.hi.picturebackend.aop;

import com.hi.picturebackend.annotation.AuthCheck;
import com.hi.picturebackend.exception.BusinessException;
import com.hi.picturebackend.exception.ErrorCode;
import com.hi.picturebackend.model.entity.User;
import com.hi.picturebackend.model.enums.UserRoleEnum;
import com.hi.picturebackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限拦截切面
 * 基于 Spring AOP，对标注了 @AuthCheck 注解的方法进行权限校验
 */
@Aspect // 声明该类为切面
@Component // 交由 Spring 容器管理
public class AuthInterceptor {

    /**
     * 用户服务
     * 用于获取当前登录用户信息
     */
    @Resource
    private UserService userService;

    /**
     * 环绕通知方法
     * 在目标方法执行前后进行权限拦截处理
     *
     * @param joinPoint 切入点，封装了目标方法的信息
     * @param authCheck 方法上标注的权限校验注解
     * @return 目标方法的执行结果
     * @throws Throwable 目标方法可能抛出的异常
     */
    @Around("@annotation(authCheck)") // 拦截所有标注了 @AuthCheck 的方法
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {

        // 从注解中获取必须具备的角色
        String mustRole = authCheck.mustRole();

        // 获取当前请求上下文
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request =
                ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 将必须具备的角色值转换为枚举类型
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);

        // 如果方法未设置必须角色，表示不进行权限限制，直接放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }

        // 以下逻辑：方法要求必须具备指定角色

        // 获取当前登录用户的角色枚举
        UserRoleEnum userRoleEnum =
                UserRoleEnum.getEnumByValue(loginUser.getUserRole());

        // 当前用户角色为空，视为无权限，直接拒绝访问
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 如果方法要求管理员权限，但当前用户不是管理员，则拒绝访问
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum)
                && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 权限校验通过，执行目标方法
        return joinPoint.proceed();
    }
}
