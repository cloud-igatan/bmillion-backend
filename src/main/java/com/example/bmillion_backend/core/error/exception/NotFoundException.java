package com.example.bmillion_backend.core.error.exception;

import com.example.bmillion_backend.core.error.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}