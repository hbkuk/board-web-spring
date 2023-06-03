package com.study.ebsoft.handler;

import com.study.ebsoft.exception.SearchConditionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 쿼리 스트링에 포함된 검색 조건이 유효하지 않은 경우 해당 예외 처리를 담당합니다.
     *
     * 예를 들어, "start_date=12313"은 유효한 형식이지만,
     *          "start_date=8월28일"은 유효하지 않은 형식이기 때문에 예외를 던집니다
     *
     * @param e   발생한 SearchConditionException 예외 객체
     * @param req HttpServletRequest 객체
     * @return 리다이렉트 URL
     * @throws UnsupportedEncodingException 인코딩 예외가 발생할 경우
     */
    @ExceptionHandler(SearchConditionException.class)
    public String handleSearchConditionException(SearchConditionException e, HttpServletRequest req) throws UnsupportedEncodingException {
        log.error(e.getMessage());

        req.removeAttribute("searchConditionQueryString");
        return "redirect:/boards";
    }

    /**
     * NoSuchElementException 예외 처리
     *
     * 예를 들어,
     *      게시글을 수정 또는 삭제 하려고 했을때, 해당 게시글이 없을 경우 예외를 던집니다.
     *
     * @param e 발생한 NoSuchElementException 예외 객체
     * @return 에러 페이지에 대한 ModelAndView 객체
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/error404");
        return modelAndView;
    }

    /**
     * 최상위 예외 처리
     *
     * @param e 발생한 Throwable 예외 객체
     * @return RuntimeException 발생을 위한 예외 처리
     */
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleThrowable(Throwable e) {
        log.error(e.getMessage());

        throw new RuntimeException("Internal Server Error");
    }
}


