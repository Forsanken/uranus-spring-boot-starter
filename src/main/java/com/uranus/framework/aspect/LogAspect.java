/*
 * FileName: OperLogs
 * Author:   QIAO
 * Date:     2018/1/5 18:00
 * Description: 操作日志注解
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.aspect;

import com.uranus.framework.annotate.ControllerLog;
import com.uranus.framework.annotate.ServiceLog;
import com.uranus.framework.jackson.JacksonUtils;
import com.uranus.framework.util.IPv4Util;
import com.uranus.framework.util.StringUtil;
import com.uranus.framework.util.UserAgentUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈操作日志注解〉
 *
 * @author CHY 2019/9/2
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * Service层切点
     */
    @Pointcut("@annotation(com.uranus.framework.annotate.ServiceLog)")
    public void serviceAspect() {

    }

    /**
     * Controller层切点
     */
    @Pointcut("@annotation(com.uranus.framework.annotate.ControllerLog)")
    public void controllerAspect() {

    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method =  joinPoint.getSignature().getName() + "()";
            //方法参数
            String methodParam = JacksonUtils.objToJson(joinPoint.getArgs());
            Map<String, String[]> params = request.getParameterMap();
            StringBuilder decode = new StringBuilder();
            //针对get请求
            if(request.getQueryString()!=null){
                try {
                    decode = new StringBuilder(URLDecoder.decode(request.getQueryString(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                //针对post请求
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    for (String value : values) {
                        decode.append(key).append("=").append(value).append("&");
                    }
                }
            }
            //将String根据&转成Map
            Map<String, Object> methodParamMap = StringUtil.convertStringToMap(decode.toString(), "&", "=");
            //设置日期格式
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //方法描述
            String methodDescription = getControllerMethodDescription(joinPoint);
            StringBuilder sb = new StringBuilder(1000);
            sb.append("\n");
            sb.append("*********************************Request请求***************************************");
            sb.append("\n");
            sb.append("ClassName     :  ").append(className).append("\n");
            sb.append("RequestMethod :  ").append(method).append("\n");
            sb.append("ContentType   :  ").append(("".equals(request.getContentType()) || request.getContentType() == null)?"FROM":request.getContentType()).append("\n");
            sb.append("RequestParams :  ").append("".equals(decode.toString()) ?methodParam:methodParamMap).append("\n");
            sb.append("RequestType   :  ").append(request.getMethod()).append("\n");
            sb.append("Description   :  ").append(methodDescription).append("\n");
            sb.append("ServerAddr    :  ").append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append("\n");
            sb.append("RemoteAddr    :  ").append(IPv4Util.getIpAddrStr(request)).append("\n");
            UserAgent userAgent = UserAgentUtils.getUserAgent(request);
            sb.append("DeviceName    :  ").append(userAgent.getOperatingSystem().getName()).append("\n");
            sb.append("BrowserName   :  ").append(userAgent.getBrowser().getName()).append("\n");
            sb.append("UserAgent     :  ").append(request.getHeader("User-Agent")).append("\n");
            sb.append("RequestUri    :  ").append(StringUtil.abbr(request.getRequestURI(), 255)).append("\n");
            sb.append("**************************");
            sb.append(df.format(LocalDateTime.now()));
            sb.append("***********************************");
            sb.append("\n");
            log.info(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterReturning(returning = "ret", pointcut = "controllerAspect()")
    public void doAfterReturning(Object ret) {
        //请求方法
        StringBuilder sb = new StringBuilder(1000);
        // 处理完请求，返回内容
        sb.append("\n");
        sb.append("Result        :  ").append(ret);
        log.info(sb.toString());
    }


    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint 切点
     * @param ex 捕获的异常
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            //类名
            String className = joinPoint.getTarget().getClass().getName();
            //请求方法
            String method =  (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            //方法描述
            String methodDescription = getServiceMthodDescription(joinPoint);
            //获取用户请求方法的参数并序列化为JSON格式字符串
            StringBuilder params = new StringBuilder();
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    params.append(JacksonUtils.objToJson(joinPoint.getArgs()[i])).append(";");
                }
            }
            StringBuilder sb = new StringBuilder(1000);
            sb.append("\n");
            sb.append("*********************************Service异常***************************************");
            sb.append("\n");
            sb.append("ClassName        :  ").append(className).append("\n");
            sb.append("Method           :  ").append(method).append("\n");
            sb.append("Params           :  ").append("[").append(params).append("]").append("\n");
            sb.append("Description      :  ").append(methodDescription).append("\n");
            sb.append("ExceptionName    :  ").append(ex.getClass().getName()).append("\n");
            sb.append("ExceptionMessage :  ").append(ex.getMessage()).append("\n");
            log.info(sb.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }

    /**
     * 获取注解中对方法的描述信息 用于service层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception 异常
     */
    private static String getServiceMthodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(ServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception 异常
     */
    private static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(ControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}