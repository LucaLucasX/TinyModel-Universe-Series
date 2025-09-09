package com.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.FileFilter;
import java.io.IOException;
import java.net.http.HttpRequest;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("过滤器初始化成功");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取本次请求的url
        String requestURL = request.getRequestURI();
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg"
        };
        //判断请求是否需要处理
        if (check(urls, requestURL)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("拦截到请求：{}", request.getRequestURI());
        if (request.getSession().getAttribute("employee") != null) {
            filterChain.doFilter(request, response);
            return;
        }
        else if (request.getSession().getAttribute("user") != null) {
            filterChain.doFilter(request, response);
            return;
        }
        //没登录
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
}

@Override
public void destroy() {
    Filter.super.destroy();
}

public boolean check(String[] urls, String requestUrl) {
    for (String url : urls) {
        boolean flag = PATH_MATCHER.match(url, requestUrl);
        if (flag) return true;
    }
    return false;
}
}
