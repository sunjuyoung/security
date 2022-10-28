package com.example.jwt_demo.security.exception;

public class RefreshTokenException extends RuntimeException{

    private ErrorCode errorCode;

    public enum ErrorCode{
        NO_ACCESS, BAD_ACCESS, NO_REFRESH, OLD_REFRESH, BAD_REFRESH
    }

    public RefreshTokenException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }


}
