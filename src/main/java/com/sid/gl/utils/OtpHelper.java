package com.sid.gl.utils;

import com.sid.gl.commons.ApiConstants;
import com.sid.gl.exceptions.GestionElectionTechnicalException;
import io.github.seyeadamaUASZ.service.OTPConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
@Slf4j
public class OtpHelper {
    private final OTPConfiguration otpConfiguration;

    public static ConcurrentHashMap<String, OTP> otpMap = new ConcurrentHashMap<>(); //utiliser pour stocker les otp
    private static final Long TimeMillis = 5 * 60000L;

    public String generateOtpWithHash(String identifier) throws NoSuchAlgorithmException {
        OTP otp = generateOtpHash(identifier);
        String rawOtp = otp.getCode();
        otpMap.computeIfAbsent(rawOtp, k -> otp);
        log.info("Generated OTP with hash value: {} for identifier: {}", otp.getHashValue(), identifier);
        return rawOtp;
    }


    private String generateOtp() {
        return otpConfiguration.generateCodeOtp(ApiConstants.FORMAT_OTP_NUMBER, ApiConstants.MINUTE, 5, 6);
    }

    private OTP generateOtpHash(String identifier) throws NoSuchAlgorithmException {
        if(null == identifier){
            throw new GestionElectionTechnicalException("Identifier cannot be null for OTP generation");
        }
        String rawOtp = generateOtp();
        String hashValue = generateHasValueWithCode(rawOtp,identifier); //
        OTP otp = new OTP();
        otp.setIdentifier(identifier);
        otp.setHashValue(hashValue);
        otp.setCode(rawOtp);
        otp.setExpirationTime(
                LocalDateTime.now().plusMinutes(5)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
        );
        return otp;
    }

    private String generateHasValueWithCode(String otp,String identifier) throws NoSuchAlgorithmException {
        if(StringUtils.isBlank(otp) || StringUtils.isBlank(identifier)){
            return null;
        }
        MessageDigest digest = MessageDigest
                .getInstance("SHA-256");
        String val = otp
                +"|"+
                identifier;
        byte[] hash = digest.digest(val.getBytes());
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2* hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean verifyOtpWithHash(String code,String identifier) throws NoSuchAlgorithmException {
        if(BooleanUtils.isFalse(otpMap.containsKey(code))){
            throw new GestionElectionTechnicalException("OTP not found for code: " + code);
        }
        long currentTime = new Date().getTime();
        OTP otp = otpMap.get(code);
        if(currentTime > otp.getExpirationTime()){
            log.error("OTP expired for code: {}", code);
            return false;
        }

        String hashValueStored = otp.getHashValue();
        String hash = generateHasValueWithCode(otp.getCode(), identifier);
        if(!StringUtils.equals(hash,hashValueStored)){
            log.error("Hash value does not match for code: {} and identifier: {}", code, identifier);
            return false;
        }
        otpMap.remove(code);
        log.info("OTP verified successfully for code: {} and identifier: {}", code, identifier);
        return true;
    }


}
