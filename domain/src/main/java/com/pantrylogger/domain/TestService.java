package com.pantrylogger.domain;

import com.pantrylogger.domain.recipes.RecipeQueryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private static Logger log = LoggerFactory.getLogger(TestService.class.getName());

    public TestService(
            RecipeQueryPort recipeQueryPort
    ) {
        log.info(recipeQueryPort.getAll().toString());
    }

}