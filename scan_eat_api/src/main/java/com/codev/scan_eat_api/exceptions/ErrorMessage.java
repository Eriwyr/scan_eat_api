package com.codev.scan_eat_api.exceptions;

public class ErrorMessage {
    private int errorCode;
    private String errorMessage;

    public ErrorMessage(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
