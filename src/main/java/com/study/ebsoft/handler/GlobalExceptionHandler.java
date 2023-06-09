package com.study.ebsoft.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ebsoft.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    /**
     * JsonProcessingException을 처리하는 예외 핸들러
     * JSON 처리 오류를 처리
     *
     * @param e JsonProcessingException 인스턴스
     * @return ErrorResponse와 HttpStatus를 포함하는 ResponseEntity
     * @throws JsonProcessingException JSON 처리 오류가 발생할 경우 예외를 던집니다.
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException e) {
        log.error("JSON 처리 오류: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.SERVER_INTERNAL_ERROR);
        errorResponse.setDetail(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * MethodArgumentNotValidException을 처리하는 예외 핸들러
     * 메서드 인자의 유효성 검증 오류를 처리
     *
     * @param e MethodArgumentNotValidException 인스턴스
     * @return ErrorResponse와 HttpStatus를 포함하는 ResponseEntity
     * @throws JsonProcessingException JSON 처리 오류가 발생할 경우 예외를 던집니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) throws JsonProcessingException {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_BOARD_DATA);

        List<String> fieldErrors = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        String fieldErrorsJson = objectMapper.writeValueAsString(fieldErrors);
        errorResponse.setDetail(fieldErrorsJson);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * BindException을 처리하는 예외 핸들러
     * 바인딩 오류를 처리
     *
     * @param e BindException 인스턴스
     * @return ErrorResponse와 HttpStatus를 포함하는 ResponseEntity
     * @throws JsonProcessingException JSON 처리 오류가 발생할 경우 예외를 던집니다.
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) throws JsonProcessingException {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_PARAM);

        List<String> fieldErrors = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        String fieldErrorsJson = objectMapper.writeValueAsString(fieldErrors);
        errorResponse.setDetail(fieldErrorsJson);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * BoardNotFoundException 예외 처리
     *
     * 예를 들어,
     *      게시글을 보기, 수정, 삭제 하려고 했을때, 해당 게시글이 없을 경우 예외를 던집니다.
     *
     * @param e 발생한 BoardNotFoundException 예외 객체
     * @return 응답 결과
     */
    // TODO: 도메인별로 구분하는 것이 아닌 => 재활용 검토
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity handleBoardNotFoundException(BoardNotFoundException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BOARD_NOT_FOUND);
        errorResponse.setDetail(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * InvalidPasswordException 예외처리
     * 
     * @param e 발생한 InvalidPasswordException 예외 객체
     * @return 응답 결과
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity handleInvalidPasswordException(InvalidPasswordException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_PASSWORD);
        errorResponse.setDetail(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * NoHandlerFoundException 예외처리
     *
     * @param e 발생한 NoHandlerFoundException 예외 객체
     * @return 응답 결과
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INVALID_REQUEST);
        errorResponse.setDetail(ErrorCode.INVALID_REQUEST.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}


