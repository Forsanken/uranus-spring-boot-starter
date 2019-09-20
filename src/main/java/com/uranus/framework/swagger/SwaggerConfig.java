package com.uranus.framework.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

@Configuration
@EnableConfigurationProperties(value = { SwaggerProperties.class })
public class SwaggerConfig {

	@Autowired
	SwaggerProperties properties;

	@Bean
	public Docket petApi() {
        Predicate<RequestHandler> predicate = input -> Objects.requireNonNull(input).isAnnotatedWith(ApiOperation.class);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(predicate::test)
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class)
                ;
	}

	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .termsOfServiceUrl(properties.getTermsOfServiceUrl())
                .license(properties.getLicense())
                .licenseUrl(properties.getLicenseUrl())
                .build();
	}

}
