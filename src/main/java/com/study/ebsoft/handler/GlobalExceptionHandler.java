package com.study.ebsoft.handler;

import com.study.ebsoft.exception.ErrorCode;
import com.study.ebsoft.exception.ErrorResponse;
import com.study.ebsoft.exception.SearchConditionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    /**
     * MethodArgumentNotValidException을 처리하는 예외 핸들러입니다.
     * 메서드 인자의 유효성 검증 오류를 처리합니다.
     *
     * @param e MethodArgumentNotValidException 인스턴스
     * @return ErrorResponse와 HttpStatus를 포함하는 ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_BOARD_DATA);

        List<String> fieldErrors = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        errorResponse.setDetail(String.join(", ", fieldErrors));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * BindException을 처리하는 예외 핸들러입니다.
     * 바인딩 오류를 처리합니다.
     *
     * @param e BindException 인스턴스
     * @return ErrorResponse와 HttpStatus를 포함하는 ResponseEntity
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_SEARCH_CONDITION);

        List<String> fieldErrors = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        errorResponse.setDetail(String.join(", ", fieldErrors));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


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
     * @return 응답 결과
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 글을 찾을 수 없습니다."); // Status Code 404
    }
}


