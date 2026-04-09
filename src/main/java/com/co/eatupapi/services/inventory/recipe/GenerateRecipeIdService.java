package com.co.eatupapi.services.inventory.recipe;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateRecipeIdService {

    public UUID run() {
        return UUID.randomUUID();
    }
}