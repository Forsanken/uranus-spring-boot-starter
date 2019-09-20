
package com.uranus.framework.swagger;

import io.github.swagger2markup.*;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class Swagger2Doc {

    @Autowired
    private SwaggerProperties properties;

    @Value("${server.port}")
    private int serverPort;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private WebApplicationContext wac;

    public void createDoc() throws Exception {
        String outputDir = properties.getFilePath();
        String host = properties.getHost();
        String fileType = Optional.ofNullable(properties.getFileType()).orElse("html5");


        Map<String, Object> map = wac.getBeansWithAnnotation(RestController.class);
        StringBuilder tagDesc = new StringBuilder("");
        if (!CollectionUtils.isEmpty(map)) {
            map.keySet().forEach(key -> tagDesc.append(Character.toUpperCase(key.charAt(0))).append(key.substring(1)).append("   "));
        }

        if (StringUtils.isEmpty(outputDir)) {
            outputDir = new File("").getCanonicalPath().replaceAll(applicationName,"");
        }
        if (StringUtils.isEmpty(outputDir)) {
            log.error("无法读取到文件路径");
            return;
        }
        if (outputDir.endsWith("\\")) {
            outputDir += "target\\swagger";
        } else {
            outputDir += "\\target\\swagger";
        }


        if (StringUtils.isEmpty(host)) {
            host = "localhost:8080";
        }

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        MvcResult mvcResult = mockMvc.perform(get("/v2/api-docs")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        String nowJsonStr = response.getContentAsString();

        Swagger swagger = new Swagger20Parser().parse(nowJsonStr);
        swagger.setHost(host);
        swagger.setBasePath("/");
        Map<String, Path> paths = Optional.ofNullable(swagger.getPaths()).orElse(new HashMap<>());
        Map<String, Path> newPaths = new HashMap<>();
        String sb = "http://" + host + contextPath;
        paths.forEach((key,value) -> newPaths.put(sb + key, value));
        swagger.setPaths(newPaths);
        if (!CollectionUtils.isEmpty(swagger.getTags())) {
            swagger.getTags().forEach(tag -> tag.setDescription(tagDesc.toString()));
        }

        Files.createDirectories(Paths.get(outputDir));

        if (Paths.get(outputDir,"swagger.json").toFile().exists()) {
            Swagger oldSwagger = new Swagger20Parser().parse(new String(Files.readAllBytes(Paths.get(outputDir,"swagger.json"))));
            if (!CollectionUtils.isEmpty(oldSwagger.getPaths())){
                oldSwagger.getPaths().forEach(swagger::path);
            }
            if (!CollectionUtils.isEmpty(oldSwagger.getDefinitions())){
                oldSwagger.getDefinitions().forEach(swagger::addDefinition);
            }
            if (!CollectionUtils.isEmpty(oldSwagger.getTags())){
                oldSwagger.getTags().forEach(swagger::addTag);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"), StandardCharsets.UTF_8)){
            writer.write(Json.pretty(swagger));
        }

        Map<String, String> configMap = new HashMap<>();
        configMap.put("swagger2markup.extensions.springRestDocs.snippetBaseUri", outputDir);
        configMap.put("swagger2markup.extensions.springRestDocs.defaultSnippets", "true");
        configMap.put(Swagger2MarkupProperties.PATHS_GROUPED_BY, GroupBy.TAGS.name());
        configMap.put(Swagger2MarkupProperties.MARKUP_LANGUAGE, MarkupLanguage.ASCIIDOC.name());
        configMap.put(Swagger2MarkupProperties.OUTPUT_LANGUAGE, Language.ZH.name());

        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder(configMap).build();
        Swagger2MarkupConverter converter = Swagger2MarkupConverter.from(swagger).withConfig(config).build();
        converter.toPath(Paths.get(outputDir));

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        Options options = new Options();
        options.setHeaderFooter(true);
        options.setMkDirs(true);
        options.setToDir(outputDir);  // 最终html文档的生成目录
        options.setBaseDir(outputDir); // asciidoc模板文件路径
        options.setBackend(fileType);
        options.setSafe(SafeMode.UNSAFE);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("source-highlighter" , "coderay");
        attributes.put("imagesdir" , "images@");
        attributes.put("attribute-missing" , "skip");
        attributes.put("attribute-undefined" , "drop-line");
        attributes.put("doctype" , "book");
        attributes.put("generated" , outputDir);  // markup文件夹路径
        attributes.put("hardbreaks" , "");
        attributes.put("icons" , "font");
        attributes.put("numbered" , "");
        attributes.put("sectlinks" , "");
        attributes.put("sectanchors" , "");
        attributes.put("toc" , "left");
        attributes.put("toclevels" , "3");
        options.setAttributes(attributes);

        //File adocFile = new File(this.getClass().getResource("/asciidoctor/index.adoc").getFile());
        String fileInfo = "include::{generated}/overview.adoc[]\n" +
                "include::{generated}/paths.adoc[]\n" +
                "include::{generated}/security.adoc[]\n" +
                "include::{generated}/definitions.adoc[]";

        File adocFile = new File("index.adoc");

        FileUtils.writeStringToFile(adocFile, fileInfo);

        asciidoctor.convertFile(adocFile, options);  //模板文件index.adoc的路径
        adocFile.deleteOnExit();
    }
}