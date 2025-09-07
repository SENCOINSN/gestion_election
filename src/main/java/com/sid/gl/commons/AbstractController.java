package com.sid.gl.commons;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AbstractController {

    public <T> ResponseEntity<ApiResponse> getResponseEntity(T response) {
        ApiResponse<T> responseDTO = ApiResponse
                .<T>builder()
                .status(Status.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
