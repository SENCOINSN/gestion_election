package com.sid.gl.utils;

import lombok.Data;

@Data
public class OTP {
    private String code;
    private Long expirationTime;
    private String identifier;
    private String transactionId;
    private String hashValue;

}
