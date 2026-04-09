package com.co.eatupapi.services.inventory.recipe;

import com.co.eatupapi.dto.inventory.recipe.RecipeResponse;
import com.co.eatupapi.repositories.inventory.recipe.RecipeRepository;
import com.co.eatupapi.utils.inventory.recipe.mapper.RecipeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListRecipesService {

    private final RecipeRepository repo;
    private final RecipeMapper mapper;

    public ListRecipesService(RecipeRepository repo, RecipeMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<RecipeResponse> run() {

        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}