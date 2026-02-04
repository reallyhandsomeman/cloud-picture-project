package com.hi.picturebackend;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan("com.hi.picturebackend.mapper") // Mapper扫描注解
@EnableAspectJAutoProxy(exposeProxy = true) // AOP注解依赖，提供对代理对象的访问
@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})
public class PictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PictureBackendApplication.class, args);
    }

}
