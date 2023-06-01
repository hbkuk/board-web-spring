package com.study.ebsoft.interceptor;

import com.study.ebsoft.utils.SearchConditionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 요청 속성에 검색 조건(SearchCondition) 쿼리 문자열을 추가하는 역할을 합니다.
 * 이는 이후 처리에서 검색 조건 쿼리 문자열에 접근하는 데 사용됩니다.
 */
public class HttpInterceptor implements HandlerInterceptor {

    /**
     * 핸들러 실행 후에 실행되며, 요청 속성을 검색 조건 쿼리 문자열을 추가합니다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param handler  실행되는 핸들러 (컨트롤러 메서드)
     * @param modelAndView 응답 뷰를 나타내는 ModelAndView 객체
     * @throws Exception 후 처리 중에 예외가 발생하는 경우
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("searchConditionQueryString", SearchConditionUtils.buildQueryString(request.getParameterMap()));
    }
}

