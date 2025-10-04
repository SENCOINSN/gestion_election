package com.sid.gl.elections.scrutins;

import com.sid.gl.exceptions.BadValidateException;
import com.sid.gl.exceptions.ElectionNotFoundException;

import java.security.NoSuchAlgorithmException;

public interface ScrutinService {
    Long vote(ScrutinVoiceRequest request,String username) throws ElectionNotFoundException, NoSuchAlgorithmException;
    String validateOtp(String otp, String username,Long bulletinId) throws ElectionNotFoundException, NoSuchAlgorithmException, BadValidateException;
}
