package com.sid.gl.commons;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractController {

    public String getCurrentUserConnected() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public <T> ResponseEntity<ApiResponse<T>> getResponseEntity(T response) {
        ApiResponse<T> responseDTO = ApiResponse
                .<T>builder()
                .status(Status.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
