/*
 * FileName: CorsFilter
 * Author:   chy
 * Date:     2018/5/24 15:10
 * Description: 跨越访问过滤器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.web.filter;

import com.uranus.framework.jackson.JacksonMapper;
import com.uranus.framework.util.IPv4Util;
import com.uranus.framework.web.enums.CommonCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈spring boot 跨域访问〉
 *
 * @author chy 2018/10/16
 * @since 1.0.0
 */
@Slf4j
public class CrossFilter implements Filter {

    @Autowired
    private JacksonMapper jacksonMapper;

    private FilterConfig filterConfig = null;

    @Value(value = "${white.list}")
    private String ipWhiteStr;

    public CrossFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        log.debug("CrossFilter:Initializing filter");
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("CrossFilter()");
        }
        return "CrossFilter(" + filterConfig +")";
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            String ip = IPv4Util.getIpAddrStr((HttpServletRequest) request);
            if (StringUtils.isEmpty(ip)) {
                throw new Exception("未获取到IP地址");
            }
            if (!ipWhiteStr.isEmpty() && !ipWhiteStr.equals("all") && !ipWhiteStr.contains(ip)) {
                throw new Exception("非法IP地址");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            sendProcessingError(e, response);
            return;
        }



        if (response instanceof HttpServletResponse) {
            HttpServletResponse alteredResponse = ((HttpServletResponse) response);
            addHeadersFor200Response(alteredResponse, (HttpServletRequest)request);
        }

        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            problem = t;
            log.error(t.getMessage());
        }

        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    @Override
    public void destroy() {

    }

    private void addHeadersFor200Response(HttpServletResponse response, HttpServletRequest request) {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        //response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        //response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, Authorization, Connection, User-Agent, Cookie, token");
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Max-Age", "1728000");
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("code", CommonCodeEnum.FAILED.getCode());
            map.put("msg", t.getMessage());
            out = response.getWriter();
            out.print(jacksonMapper.mapToJson(map));
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
