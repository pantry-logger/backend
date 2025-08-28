
package com.pantrylogger.restapi;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pantrylogger.domain.recipes.GetAllRecipesUseCase;

@RestController
@RequestMapping("recipes")
public class RecipesController {

    private final GetAllRecipesUseCase getAllRecipesUseCase;

    public RecipesController(
            GetAllRecipesUseCase getAllRecipesUseCase) {
        this.getAllRecipesUseCase = getAllRecipesUseCase;
    }

    @GetMapping
    public List<String> findAll() {
        return this.getAllRecipesUseCase.getAllRecipes();
    }
}
