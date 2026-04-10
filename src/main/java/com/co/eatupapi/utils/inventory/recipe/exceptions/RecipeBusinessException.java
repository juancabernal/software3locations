package com.co.eatupapi.utils.inventory.recipe.exceptions;

public class RecipeBusinessException extends RecipeException {

    public RecipeBusinessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}