package com.sid.gl.auth;

import com.sid.gl.commons.AbstractController;
import com.sid.gl.commons.ApiConstants;
import com.sid.gl.commons.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ApiConstants.BASE_PATH+"/auth",produces = "application/json;charset=UTF-8")
public class AuthController extends AbstractController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //todo api login

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@RequestBody AuthRequestDto authRequestDto){
        return getResponseEntity(authService.authenticate(authRequestDto));
    }

}
