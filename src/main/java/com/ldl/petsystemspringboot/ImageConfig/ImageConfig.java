package com.ldl.petsystemspringboot.ImageConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ImageConfig extends WebMvcConfigurationSupport {

    //将方法的返回值添加到容器中；容器中这个组件默认的id就是方法名
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/ static/");
        registry.addResourceHandler("/device/**").addResourceLocations("classpath:/static/device/");
//        registry.addResourceHandler("/images/**").addResourceLocations("file:/D:/upload-images/" );
        registry.addResourceHandler("/images/**").addResourceLocations("file:/D:/images/" );
    }
}

