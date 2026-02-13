package com.chatapp.exception;

public class CustomExceptions {

    public static class InvalidOtpException extends RuntimeException {
        public InvalidOtpException(String message) {
            super(message);
        }
    }

    public static class OtpExpiredException extends RuntimeException {
        public OtpExpiredException(String message) {
            super(message);
        }
    }

    public static class TooManyOtpRequestsException extends RuntimeException {
        public TooManyOtpRequestsException(String message) {
            super(message);
        }
    }
}