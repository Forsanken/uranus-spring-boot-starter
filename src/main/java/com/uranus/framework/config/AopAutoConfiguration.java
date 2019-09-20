package com.uranus.framework.config;

import com.uranus.framework.aspect.LogAspect;
import com.uranus.framework.aspect.ValidAspect;
import org.springframework.aop.framework.AopContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 〈一句话功能简述〉<br>
 * 〈切面自动配置〉
 *
 * @author chy 2019/9/3
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(AopContext.class)
@ConditionalOnProperty(prefix = "uranus", name = "aop.enable", matchIfMissing = true)
@Import({LogAspect.class, ValidAspect.class})
public class AopAutoConfiguration {

}