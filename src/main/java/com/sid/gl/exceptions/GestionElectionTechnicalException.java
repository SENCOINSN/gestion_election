package com.sid.gl.exceptions;

public class GestionElectionTechnicalException extends RuntimeException{
    public GestionElectionTechnicalException(String message) {
        super(message);
    }
    public GestionElectionTechnicalException(String message,Throwable cause) {
        super(message,cause);
    }
    public GestionElectionTechnicalException(String message,Throwable cause,boolean enableSuppression,boolean writableStackTrace) {
        super(message,cause,enableSuppression,writableStackTrace);
    }
    public GestionElectionTechnicalException(Throwable cause) {
        super(cause);
    }

}
