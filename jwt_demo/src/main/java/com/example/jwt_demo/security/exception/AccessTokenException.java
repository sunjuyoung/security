package com.example.jwt_demo.security.exception;

public class AccessTokenException extends RuntimeException{


    TOKEN_ERROR token_error;

    public enum TOKEN_ERROR{



    }
}
