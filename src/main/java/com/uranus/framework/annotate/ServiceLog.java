/**
 * FileName: ServiceLog
 * Author:   chy
 * Date:     2019/9/2 14:56
 * Description: Service日志切面注解
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.annotate;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Service日志切面注解〉
 *
 * @author chy
 * @date 2019/9/2
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceLog {
    /**
     * 描述
     */
    String description() default "";
}
