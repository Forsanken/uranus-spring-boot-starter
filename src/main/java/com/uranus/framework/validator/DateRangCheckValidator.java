/*
 * FileName: DateAfterSecDateValidator
 * Author:   QIAO
 * Date:     2018/5/11 10:06
 * Description: 注解@DateAfterSecDate的校验实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.validator;

import com.uranus.framework.annotate.DateRangCheck;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈注解@DateAfterSecDate的校验实现类〉
 *
 * @author CHY 2018/5/11
 * @since 1.0.0
 */
@Slf4j
public class DateRangCheckValidator implements ConstraintValidator<DateRangCheck, Object> {

    /**
     * 开始日期字段名称
     */
    private String beginField;
    /**
     * 结束日期字段名称
     */
    private String endField;

    @Override
    public void initialize(DateRangCheck dateRangCheck) {
        this.beginField = dateRangCheck.beginField();
        this.endField = dateRangCheck.endField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        Class beginClass;
        Class endClass;

        try {
            beginClass = PropertyUtils.getPropertyType(o, beginField);
            endClass = PropertyUtils.getPropertyType(o, endField);
        } catch (Exception e) {
            StringBuilder errorInfo = new StringBuilder("");
            errorInfo.append(o.getClass().getName()).append("对象");
            errorInfo.append("读取").append(beginField).append(" or ").append(endField).append("的类型出错!");

            log.error(errorInfo.toString());
            log.error(e.getMessage() );

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorInfo.toString()).addBeanNode().addConstraintViolation();
            return false;
        }

        if (beginClass != endClass) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("数据类型不一致无法比较!").addBeanNode().addConstraintViolation();
            return false;
        }

        Object beginObj;
        Object endObj;
        try {
            beginObj = PropertyUtils.getProperty(o, beginField);
            endObj = PropertyUtils.getProperty(o, endField);
            if (null == beginObj || null == endObj) {
                throw new Exception("未读取到属性值");
            }
        } catch (Exception e) {
            StringBuilder errorInfo = new StringBuilder("");
            errorInfo.append(o.getClass().getSimpleName()).append("对象");
            errorInfo.append("读取").append(beginField).append(" or ").append(endField).append("的值出错!");

            log.error(errorInfo.toString());
            log.error(e.getMessage() );

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorInfo.toString()).addBeanNode().addConstraintViolation();
            return false;
        }

        if (beginClass == Date.class) {
            return ((Date) endObj).getTime() - ((Date) beginObj).getTime() >= 0;
        }

        if (beginClass == LocalDate.class) {
            return !((LocalDate) endObj).isBefore(((LocalDate) beginObj));
        }

        if (beginClass == LocalTime.class) {
            return !((LocalTime) endObj).isBefore(((LocalTime) beginObj));
        }

        if (beginClass == LocalDateTime.class) {
            return !((LocalDateTime) endObj).isBefore(((LocalDateTime) beginObj));
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("非时间类型,无法比较").addBeanNode().addConstraintViolation();
        return false;
    }
}