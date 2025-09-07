package com.sid.gl.users;

import com.sid.gl.commons.AbstractController;
import com.sid.gl.commons.ApiConstants;
import com.sid.gl.commons.ApiResponse;
import com.sid.gl.exceptions.UserAlreadyExistException;
import com.sid.gl.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ApiConstants.BASE_PATH+"/users")
@Slf4j
public class UserController extends AbstractController {
    private final UserService userService; //couplage faible

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Inscription d'un utilisateur")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody @Valid UserRequestDto request) throws UserAlreadyExistException {
        return getResponseEntity(userService.register(request));
    }

    @Operation(summary = "recuperation la liste des utilisateurs")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers(
        @RequestParam(name = "page",defaultValue = ApiConstants.PAGE)int page,

        @RequestParam(name = "size",defaultValue = ApiConstants.SIZE)int size
    ) {
        return getResponseEntity(userService.getAllUsers(page,size));
    }

    @Operation(summary = "recuperation d'un utilisateur depuis l'id")
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getUser(
            @Parameter(name = "id",required = true)
            @PathVariable Long id) throws UserNotFoundException {
        return getResponseEntity(userService.getUser(id));
    }

}
