package com.co.eatupapi.services.inventory.recipe;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculateRecipeSellingPriceService {

    private static final BigDecimal ONE_HUNDRED =
            BigDecimal.valueOf(100);

    private static final int SCALE = 4;

    public BigDecimal run(
            BigDecimal baseCost,
            Integer profitMargin
    ) {

        BigDecimal margin =
                BigDecimal.valueOf(
                        profitMargin
                ).divide(
                        ONE_HUNDRED,
                        SCALE,
                        RoundingMode.HALF_UP
                );

        return baseCost.add(
                baseCost.multiply(margin)
        );
    }
}