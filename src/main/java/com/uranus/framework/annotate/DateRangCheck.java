/*
 * FileName: DateAfterSecDate
 * Author:   QIAO
 * Date:     2018/5/11 9:50
 * Description: 当前时间必须晚于另一个时间
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.annotate;

import com.uranus.framework.validator.DateRangCheckValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈当前时间必须晚于另一个时间〉
 *
 * @author QIAO
 * @create 2018/5/11
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = DateRangCheckValidator.class)
@Target( {ElementType.TYPE,ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRangCheck {

    /**
     * 开始日期字段名称
     * @return String
     */
    String beginField() default "beginDate";

    /**
     * 结束日期字段名称
     * @return String
     */
    String endField() default "endDate";

    String message() default "{beginField}必须早于{endField}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
