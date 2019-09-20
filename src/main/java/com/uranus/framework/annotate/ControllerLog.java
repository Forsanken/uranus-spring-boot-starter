/**
 * FileName: ControllerLog
 * Author:   chy
 * Date:     2019/9/2 14:55
 * Description: Controller日志切面
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.annotate;

import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Controller日志切面注解〉
 *
 * @author chy
 * @date 2019/9/2
 * @since 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerLog {
    /**
     * 描述
     */
    String description() default "";
}
