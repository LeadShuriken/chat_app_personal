package com.chat.annotations;

import javax.validation.Payload;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Constraint(validatedBy = SQLInjectionSafeValidator.class)
public @interface SQLInjectionSafe {
    String message() default "{SQLInjectionSafe}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}