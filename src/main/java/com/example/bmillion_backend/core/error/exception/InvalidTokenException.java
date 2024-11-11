package com.example.bmillion_backend.core.error.exception;

import com.example.bmillion_backend.core.error.ErrorCode;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
