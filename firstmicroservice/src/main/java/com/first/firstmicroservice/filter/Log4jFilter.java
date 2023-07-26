package com.first.firstmicroservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
public class Log4jFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(Log4jFilter.class);
    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Log4jFilter initialized for {} ", applicationName);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //The only purpose of this filter is to clear the thread context for logging.
        //This should be the first filter invoked, that way it performs the cleanup after all the filters.
        //in the chain have completed execution

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
