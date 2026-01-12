package com.hi.picturebackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Spring MVC JSON 配置类
 * 用于自定义 Jackson 的序列化规则
 */
@JsonComponent // 注册 Jackson 相关组件，使配置自动生效
public class JsonConfig {

    /**
     * 自定义 ObjectMapper Bean
     * 解决 Long / long 类型在序列化为 JSON 时的精度丢失问题
     *
     * @param builder Jackson ObjectMapper 构建器，包含 Spring Boot 默认配置
     * @return 配置完成的 ObjectMapper
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {

        // 基于 Spring Boot 默认配置创建 ObjectMapper（不启用 XML 支持）
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 创建 Jackson 模块，用于注册自定义序列化器
        SimpleModule module = new SimpleModule();

        // 将 Long 包装类型序列化为字符串，防止前端精度丢失
        module.addSerializer(Long.class, ToStringSerializer.instance);

        // 将 long 基本类型序列化为字符串，防止前端精度丢失
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 将自定义模块注册到 ObjectMapper 中
        objectMapper.registerModule(module);

        // 返回配置完成的 ObjectMapper，供 Spring MVC 使用
        return objectMapper;
    }
}
