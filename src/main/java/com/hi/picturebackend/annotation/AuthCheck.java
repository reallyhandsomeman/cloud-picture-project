package com.hi.picturebackend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解
 * 用于标记在方法上，表示调用该方法前需要进行权限/角色校验
 */
@Target(ElementType.METHOD) // 指定该注解只能作用在「方法」上
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时保留，可通过反射或 AOP 读取
public @interface AuthCheck {

    /**
     * 必须具备的角色标识
     * 例如："admin"、"user"
     * 如果为空字符串，表示不限制角色
     */
    String mustRole() default "";
}
