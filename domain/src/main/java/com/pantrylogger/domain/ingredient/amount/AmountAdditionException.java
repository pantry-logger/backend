package com.pantrylogger.domain.ingredient.amount;

import com.pantrylogger.domain.exception.AmountException;

public class AmountAdditionException extends AmountException {
    public AmountAdditionException(String message) {
        super(message);
    }
}
