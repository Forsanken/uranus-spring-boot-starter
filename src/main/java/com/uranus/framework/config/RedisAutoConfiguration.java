package com.uranus.framework.config;

import com.uranus.framework.redis.RedisConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 〈一句话功能简述〉<br>
 * 〈Redis 自动装载类〉
 *
 * @author chy 2019/9/3
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = "uranus", name = "redis.enable", matchIfMissing = true)
@Import({RedisConfig.class})
public class RedisAutoConfiguration {

}