package com.sid.gl.elections.bulletins;

import com.sid.gl.commons.AbstractController;
import com.sid.gl.commons.ApiConstants;
import com.sid.gl.commons.ApiResponse;
import com.sid.gl.exceptions.ElectionNotFoundException;
import com.sid.gl.exceptions.GestionElectionNotFoundException;
import com.sid.gl.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping(ApiConstants.BASE_PATH+"/bulletins")
public class BulletinController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(BulletinController.class);

    private final BulletinService bulletinService;

    public BulletinController(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    @PreAuthorize("hasRole('SUPERVISOR')")
    @Operation(summary = "creation du bulletin du candidat")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createBulletinCandidat(@Valid @RequestBody BulletinRequestDto bulletinRequestDto) throws UserNotFoundException, ElectionNotFoundException, RoleNotFoundException, UserNotFoundException, ElectionNotFoundException, RoleNotFoundException {
        String principal = getCurrentUserConnected();
        logger.info("BulletinController: createBulletinCandidat");
        logger.info("user principal {} ", principal);
        return getResponseEntity(bulletinService.createBulletinCandidat(bulletinRequestDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllBulletin() {
        return getResponseEntity(bulletinService.getAllBulletin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBulletin(@PathVariable Long id) throws GestionElectionNotFoundException {
        return getResponseEntity(bulletinService.getBulletin(id));
    }
}
