package com.example.bmillion_backend.core.error.exception;

import com.example.bmillion_backend.core.error.ErrorCode;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message,ErrorCode errorCode) {
        super(message, errorCode);
    }

}