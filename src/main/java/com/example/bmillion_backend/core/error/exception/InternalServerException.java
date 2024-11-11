package com.example.bmillion_backend.core.error.exception;

import com.example.bmillion_backend.core.error.ErrorCode;

public class InternalServerException extends BusinessException {

    public InternalServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
