package com.uranus.framework.config;

import com.uranus.framework.redis.RedisConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnection;

/**
 * 〈一句话功能简述〉<br>
 * 〈Redis 自动装载类〉
 *
 * @author chy 2019/9/3
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(RedisConnection.class)
@ConditionalOnProperty(prefix = "uranus", name = "redis.enable", matchIfMissing = true)
@Import({RedisConfig.class})
public class RedisAutoConfiguration {

}