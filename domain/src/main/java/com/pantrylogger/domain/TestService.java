package com.pantrylogger.domain;

import org.springframework.stereotype.Service;

import com.pantrylogger.domain.recipes.RecipeQueryPort;

@Service
public class TestService {
    public TestService(
        RecipeQueryPort recipeQueryPort
    ) {
        System.out.println(recipeQueryPort.getAll().toString());
    }
}