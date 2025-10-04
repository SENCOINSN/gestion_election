package com.sid.gl.elections.scrutins;

import com.sid.gl.commons.AbstractController;
import com.sid.gl.commons.ApiConstants;
import com.sid.gl.commons.ApiResponse;
import com.sid.gl.exceptions.BadValidateException;
import com.sid.gl.exceptions.ElectionNotFoundException;
import com.sid.gl.utils.OtpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(ApiConstants.BASE_PATH+"/scrutins")
@RequiredArgsConstructor
public class ScrutinController extends AbstractController {
    private final ScrutinService scrutinService;

    @PostMapping("/vote")
    public ResponseEntity<ApiResponse<Long>> vote(@RequestBody ScrutinVoiceRequest request) throws ElectionNotFoundException, NoSuchAlgorithmException {
        String username = getCurrentUserConnected();
        return getResponseEntity(scrutinService.vote(request, username));
    }

    @PostMapping("/validate-otp/{bulletinId}")
    public ResponseEntity<ApiResponse<String>> validate(@RequestBody OtpRequest request,
      @PathVariable("bulletinId")Long bulletinId) throws ElectionNotFoundException, NoSuchAlgorithmException, BadValidateException {
        String username = getCurrentUserConnected();
        return getResponseEntity(scrutinService.validateOtp(request.code(), username,bulletinId));
    }

}
