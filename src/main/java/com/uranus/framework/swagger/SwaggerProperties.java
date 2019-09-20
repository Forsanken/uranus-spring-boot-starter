package com.uranus.framework.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ConfigurationProperties(prefix = "uranus.swagger")
public class SwaggerProperties implements Serializable {
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 条款地址，公司内部使用的话不需要配
     */
    private String termsOfServiceUrl;
    /**
     * 协议名称
     */
    private String license;
    /**
     * 协议地址
     */
    private String licenseUrl;
    /**
     * 版本
     */
    private String version;
    /**
     * 项目地址 + 端口 示例 localhost:8080
     */
    private String host;
    /**
     * Swagger生成文件存放路径
     */
    private String filePath;
    /**
     * 生成文件的类型 html5 / pdf
     */
    private String fileType;

}
