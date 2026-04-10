package com.co.eatupapi.utils.inventory.recipe.exceptions;

public class RecipeNotFoundException extends RecipeException {

  public RecipeNotFoundException(String message) {
    super(ErrorCode.RECIPE_NOT_FOUND, message);
  }
}