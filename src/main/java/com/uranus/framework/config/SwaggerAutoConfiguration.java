package com.uranus.framework.config;

import com.uranus.framework.swagger.ResponseOperationBuilder;
import com.uranus.framework.swagger.Swagger2Doc;
import com.uranus.framework.swagger.SwaggerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 〈一句话功能简述〉<br>
 * 〈Swagger 启动配置类〉
 *
 * @author chy 2019/9/3
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "uranus", name = "swagger.enable", havingValue = "true")
@Import({SwaggerConfig.class, Swagger2Doc.class})
@EnableSwagger2
public class SwaggerAutoConfiguration {

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1002)
    public ResponseOperationBuilder customOperationBuilder() {
        return new ResponseOperationBuilder();
    }
}