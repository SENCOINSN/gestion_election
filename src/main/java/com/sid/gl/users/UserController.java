package com.sid.gl.users;

import com.sid.gl.commons.AbstractController;
import com.sid.gl.commons.ApiConstants;
import com.sid.gl.commons.ApiResponse;
import com.sid.gl.commons.DataResponse;
import com.sid.gl.exceptions.RoleNotFoundException;
import com.sid.gl.exceptions.UserAlreadyExistException;
import com.sid.gl.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ApiResponse<UserResponseDto>> register(@RequestBody @Valid UserRequestDto request) throws UserAlreadyExistException {
        return getResponseEntity(userService.register(request));
    }


    @Operation(summary = "recuperation la liste des utilisateurs")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")  // admin
    public ResponseEntity<ApiResponse<DataResponse>> getAllUsers(
        @RequestParam(name = "page",defaultValue = ApiConstants.PAGE)int page,

        @RequestParam(name = "size",defaultValue = ApiConstants.SIZE)int size
    ) {
        String principal = getCurrentUserConnected();
        log.info("User connected: {}",principal);
        return getResponseEntity(userService.getAllUsers(page,size));
    }

    @Operation(summary = "recuperation d'un utilisateur depuis l'id")
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(
            @Parameter(name = "id",required = true)
            @PathVariable Long id) throws UserNotFoundException {
        return getResponseEntity(userService.getUser(id));
    }

    //ajouter de api-addRole
    @Operation(summary = "Ajout d'un role a un utilisateur")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addRole/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> addRole(
            @Parameter(name = "id",required = true)
            @PathVariable Long id,
            @RequestBody @Valid RoleRequestDto request) throws UserNotFoundException, RoleNotFoundException {
        return getResponseEntity(userService.addRole(id,request));
    }

    //todo liste des candidats (admin)
    //todo liste des electeurs (admin)
    //todo liste des superviseurs (admin)

    @Operation(summary = "recuperation la liste des electeurs")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/electeurs")
    public ResponseEntity<ApiResponse<DataResponse>> getAllElectors(
            @RequestParam(name = "page",defaultValue = ApiConstants.PAGE)int page,

            @RequestParam(name = "size",defaultValue = ApiConstants.SIZE)int size){
        return  getResponseEntity(userService.getAllElectors(page,size));

    }

    @Operation(summary = "recuperation la liste des candidats")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/candidats")
    public ResponseEntity<ApiResponse<DataResponse>> getAllCandidats(
            @RequestParam(name = "page", defaultValue = ApiConstants.PAGE) int page,
            @RequestParam(name = "size", defaultValue = ApiConstants.SIZE)int size){
        return getResponseEntity(userService.getAllCandidats(page,size));
    }

    @Operation(summary = "recuperation la liste des superviseurs")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/superviseurs")
    public ResponseEntity<ApiResponse<DataResponse>> getAllSupervisors(
            @RequestParam(name = "page", defaultValue = ApiConstants.PAGE) int page,
            @RequestParam(name = "size", defaultValue = ApiConstants.SIZE)int size){
        return getResponseEntity(userService.getAllSupervisors(page,size));
    }

}
