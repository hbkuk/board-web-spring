package com.study.ebsoft.context;

import com.study.ebsoft.interceptor.HttpInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 스프링 MVC 구성을 정의하는 설정 클래스입니다
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 인터셉터를 등록합니다
     * 모든 경로(/**)에 대해 동작하도록 설정합니다
     * 
     * @param registry 인터셉터 레지스트리
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor())
                .addPathPatterns("/**");
    }
}
