package com.co.eatupapi.utils.commercial.purchase.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ApiErrorResponse {

    private String errorCode;
    private String message;
    private int status;
    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiErrorResponse(PurchaseErrorCode errorCode, String message, int status, String path) {
        this.errorCode = errorCode.name();
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

}