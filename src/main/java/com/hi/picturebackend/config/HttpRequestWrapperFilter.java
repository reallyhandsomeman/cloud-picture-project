package com.hi.picturebackend.config;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 请求包装过滤器
 * 作用：
 * 1. 在请求进入 Spring MVC 之前，对 HttpServletRequest 进行统一处理
 * 2. 针对 application/json 请求，使用自定义 RequestWrapper 包装 request
 * 3. 解决请求体（body）只能读取一次的问题，使后续链路可重复读取 body
 * 使用场景：
 * - 过滤器 / 拦截器 / AOP 中需要读取请求 body（如鉴权、日志、权限校验）
 * - Controller 中仍然需要通过 @RequestBody 正常接收参数
 * 注意：
 * - 只对 JSON 请求进行包装，避免不必要的性能和内存开销
 *
 * @author pine
 */
@Order(1) // 过滤器执行顺序，值越小优先级越高，确保尽早包装 request
@Component
public class HttpRequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {

        // 仅处理 HTTP 请求，避免非 HTTP 场景下发生类型转换异常
        if (request instanceof HttpServletRequest) {

            HttpServletRequest servletRequest = (HttpServletRequest) request;

            // 获取请求的 Content-Type，用于判断是否为 JSON 请求
            String contentType = servletRequest.getHeader(Header.CONTENT_TYPE.getValue());

            // 只对 application/json 请求进行包装
            // JSON 请求的参数通常位于 request body 中，且 body 只能读取一次
            if (ContentType.JSON.getValue().equals(contentType)) {

                // 使用自定义 RequestWrapper 包装原始 request
                // RequestWrapper 会在构造时缓存请求体，
                // 并重写 getInputStream / getReader，使 body 可重复读取
                //
                // 注意：
                // 这里必须在 Filter 层完成包装，
                // 才能保证后续拦截器、Controller、@RequestBody 都能生效
                chain.doFilter(new RequestWrapper(servletRequest), response);

            } else {
                // 非 JSON 请求（如 GET、form-data、文件上传等）
                // 不需要读取 body，直接放行，避免额外开销
                chain.doFilter(request, response);
            }

        } else {
            // 非 HttpServletRequest，直接放行
            chain.doFilter(request, response);
        }
    }
}
