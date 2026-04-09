package com.co.eatupapi.utils.inventory.recipe.exceptions;

public abstract class RecipeException extends RuntimeException {

    private final ErrorCode errorCode;

    public RecipeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}