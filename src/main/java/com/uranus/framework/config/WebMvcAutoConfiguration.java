package com.uranus.framework.config;

import com.uranus.framework.web.controller.BaseController;
import com.uranus.framework.web.filter.CrossFilter;
import com.uranus.framework.web.support.WebMvcSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 〈一句话功能简述〉<br>
 * 〈web mvc 配置启动类〉
 *
 * @author chy 2019/9/3
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "uranus",name = "web.enable", havingValue = "true")
@Import({WebMvcSupport.class, BaseController.class})
public class WebMvcAutoConfiguration {

    @Bean
    public CrossFilter crossFilter() {
        return new CrossFilter();
    }
}