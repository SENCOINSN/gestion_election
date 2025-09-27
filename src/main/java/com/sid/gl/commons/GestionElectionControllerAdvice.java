package com.sid.gl.commons;


import com.sid.gl.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GestionElectionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentException(MethodArgumentNotValidException exception){
        ApiResponse<?> apiResponse = new ApiResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> {
                    ErrorDTO errorDTO = new ErrorDTO(fieldError.getField(), fieldError.getDefaultMessage());
                    errors.add(errorDTO);
                });

        apiResponse.setStatus(Status.ERROR);
        apiResponse.setErrorDTOS(errors);
        return apiResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleServiceException(UserNotFoundException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(Status.ERROR);
        return response;
    }

    @ExceptionHandler(ElectionAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleServiceException(ElectionAlreadyExistException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(Status.ERROR);
        return response;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleServiceException(UserAlreadyExistException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(Status.ERROR);
        return response;
    }

    @ExceptionHandler(ElectionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleServiceException(ElectionNotFoundException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(Status.ERROR);
        return response;
    }


    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleServiceException(BadCredentialsException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(Status.ERROR);
        return response;
    }

    @ExceptionHandler(GestionElectionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleServiceException(GestionElectionNotFoundException exception){
        ApiResponse<?> response = new ApiResponse<>();
        response.setErrorDTOS(Collections.singletonList(new ErrorDTO("", exception.getMessage())));
        response.setStatus(Status.ERROR);
        return response;
    }
}
