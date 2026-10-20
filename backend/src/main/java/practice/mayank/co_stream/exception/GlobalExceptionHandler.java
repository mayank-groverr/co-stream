package practice.mayank.co_stream.exception;

import org.springframework.http.HttpStatus;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import practice.mayank.co_stream.exception.customexceptions.AlreadyPresentException;
import practice.mayank.co_stream.exception.customexceptions.BadRequestException;
import practice.mayank.co_stream.exception.customexceptions.ResourceNotFoundException;
import practice.mayank.co_stream.security.exceptions.TokenExpiredException;
import practice.mayank.co_stream.security.exceptions.RoleNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler
    public ProblemDetail resourceNotFoundExceptionHandler(ResourceNotFoundException ex, WebRequest request){
        return ErrorResponseUtil.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    public ProblemDetail RoleNotFoundExceptionHandler(RoleNotFoundException ex, WebRequest request){
        return ErrorResponseUtil.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    public ProblemDetail badRequestExceptionHandler(BadRequestException ex, WebRequest request){
        return ErrorResponseUtil.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    public ProblemDetail tokenExpiredExceptionHandler(TokenExpiredException ex, WebRequest request){
        return ErrorResponseUtil.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler
    public ProblemDetail alreadyPresentExceptionHandler(AlreadyPresentException ex, WebRequest request){
        return ErrorResponseUtil.of(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

}
