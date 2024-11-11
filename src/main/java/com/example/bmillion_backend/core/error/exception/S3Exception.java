package com.example.bmillion_backend.core.error.exception;

import com.example.bmillion_backend.core.error.ErrorCode;

public class S3Exception extends BusinessException {

    public S3Exception(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}