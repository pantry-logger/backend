package com.pantrylogger.domain.recipe;

import com.pantrylogger.domain.exception.AccessDeniedException;

public class RecipeAccessDeniedException extends AccessDeniedException {
    public RecipeAccessDeniedException() {
        super("Only the owner of the recipe may modify it.");
    }
}
