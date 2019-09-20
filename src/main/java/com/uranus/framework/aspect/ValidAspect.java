/*
 * FileName: ValidAspect
 * Author:   QIAO
 * Date:     2018/5/7 16:09
 * Description: 校验切面
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.aspect;

import com.uranus.framework.web.enums.CommonCodeEnum;
import com.uranus.framework.web.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br> 
 * 〈校验切面〉
 *
 * @author CHY 2018/5/7
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class ValidAspect {

    @Pointcut("execution(public * com..controller..*.*(..))")
    public void valid() {
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("valid()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
            //取参数，如果没参数，那肯定不校验了
            Object[] objects = pjp.getArgs();
            if (objects.length == 0) {
                return pjp.proceed();
            }
            //**************************校验封装好的javabean**********************/
            //寻找带BindingResult参数的方法，然后判断是否有error，如果有则是校验不通过
            for (Object object : objects) {
                if (object instanceof BeanPropertyBindingResult) {
                    //有校验
                    BeanPropertyBindingResult result = (BeanPropertyBindingResult) object;
                    if (result.hasErrors()) {
                        List<ObjectError> list = result.getAllErrors();
                        StringBuilder sb = new StringBuilder("请求参数错误:");
                        for (ObjectError error : list) {
                            if (error instanceof FieldError) {
                                FieldError fError = (FieldError) error;
                                sb.append(fError.getField());
                                if (Objects.requireNonNull(fError.getDefaultMessage()).length() < 100){
                                    sb.append("(").append(fError.getDefaultMessage()).append(")");
                                } else {
                                    sb.append("(").append(fError.getRejectedValue()).append(")");
                                }
                            } else {
                                sb.append(error.getObjectName());
                                if (Objects.requireNonNull(error.getDefaultMessage()).length() < 100){
                                    sb.append("(").append(error.getDefaultMessage()).append(")");
                                } else {
                                    sb.append("(").append("数据验证失败").append(")");
                                }
                            }

                            sb.append(";");
                        }
                        Result res = new Result(CommonCodeEnum.FAILED);
                        res.setMsg(sb.toString());
                        log.error(sb.toString());
                        return res;
                    }
                }
            }

            //**************************校验普通参数*************************/
            //  获得切入目标对象
            Object target = pjp.getThis();
            // 获得切入的方法
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            // 执行校验，获得校验结果
            Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, objects);
            //如果有校验不通过的
            if (!validResult.isEmpty()) {
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method); // 获得方法的参数名称

                StringBuffer sb = new StringBuffer("请求参数错误:");
                for(ConstraintViolation<Object> constraintViolation : validResult) {
                    PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();  // 获得校验的参数路径信息
                    int paramIndex = pathImpl.getLeafNode().getParameterIndex(); // 获得校验的参数位置
                    String paramName = Objects.requireNonNull(parameterNames)[paramIndex];  // 获得校验的参数名称
                    //校验信息
                    sb.append(paramName).append("(");
                    if (constraintViolation.getMessage().length() > 100 ) {
                        sb.append(constraintViolation.getInvalidValue()).append(");");
                    } else {
                        sb.append(constraintViolation.getMessage()).append(");");
                    }
                }
                Result res = new Result(CommonCodeEnum.FAILED);
                res.setMsg(sb.toString());
                log.error(sb.toString());
                return res;
            }
            return pjp.proceed();
    }

    private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        return validator.validateParameters(obj, method, params);
    }
}