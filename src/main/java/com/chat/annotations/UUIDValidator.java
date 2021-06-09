package com.chat.annotations;

import java.util.UUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<UUIDA, UUID> {
    private final String regex = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    @Override
    public void initialize(UUIDA validUuid) {
    }

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext cxt) {
        return uuid == null ? true : uuid.toString().matches(this.regex);
    }
}