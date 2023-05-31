package com.study.ebsoft.interceptor;

import com.study.ebsoft.utils.SearchConditionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(request.getParameterMap()));
    }
}
