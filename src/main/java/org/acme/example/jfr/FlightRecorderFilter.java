package org.acme.example.jfr;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FlightRecorderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        RestEndpointInvocationEvent event = new RestEndpointInvocationEvent();

        if (!event.isEnabled()) {
            return;
        }

        event.begin();

        filterConfig.getServletContext().setAttribute(RestEndpointInvocationEvent.NAME, event);
        Filter.super.init(filterConfig);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        RestEndpointInvocationEvent event = (RestEndpointInvocationEvent) httpRequest.getServletContext().getAttribute(RestEndpointInvocationEvent.NAME);

        if (event == null || !event.isEnabled()) {
            return;
        }
        event.end();
        event.path = httpRequest.getRequestURI().replaceAll("/([a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}?)", "");

        if (event.shouldCommit()) {
            event.method = httpRequest.getMethod();
            event.mediaType = String.valueOf(servletRequest.getContentType());
            event.length = httpRequest.getContentLength();
            event.queryParameters = httpRequest.getQueryString();
            event.headers = httpRequest.getHeaderNames().toString();
            event.responseLength = httpResponse.getOutputStream().toString().length();
            event.responseHeaders = httpResponse.getHeaderNames().toString();
            event.status = httpResponse.getStatus();
            event.commit();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
