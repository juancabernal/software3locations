package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.domain.inventory.recipe.RecipeDomain;
import com.co.eatupapi.utils.inventory.recipe.exceptions.ErrorCode;
import com.co.eatupapi.utils.inventory.recipe.exceptions.RecipeBusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecipeValidatorService {

    private final Validator validator;

    public RecipeValidatorService(Validator validator) {
        this.validator = validator;
    }

    public void validate(RecipeDomain recipe) {

        Set<ConstraintViolation<RecipeDomain>> violations = validator.validate(recipe);

        if (!violations.isEmpty()) {

            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            throw new RecipeBusinessException(
                    ErrorCode.VALIDATION_ERROR,
                    errorMessage
            );
        }
    }
}