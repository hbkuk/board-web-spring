package com.study.ebsoft.handler;

import com.study.ebsoft.exception.SearchConditionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SearchConditionException.class)
    public ModelAndView handleSearchConditionException(SearchConditionException e, ModelAndView mav, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        log.error(e.getMessage());

        // TODO : NestedServletException 감싸진 예외 발생으로 인한 주석처리
        //String encodedErrorMessage = URLEncoder.encode(e.getMessage(), "UTF-8");
        //redirectAttributes.addFlashAttribute("error", encodedErrorMessage);

        mav.setViewName("redirect:/boards");
        return mav;
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/error404");
        return modelAndView;
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView handleThrowable(Throwable e) {
        log.error(e.getMessage());

        throw new RuntimeException("Internal Server Error");
    }
}


