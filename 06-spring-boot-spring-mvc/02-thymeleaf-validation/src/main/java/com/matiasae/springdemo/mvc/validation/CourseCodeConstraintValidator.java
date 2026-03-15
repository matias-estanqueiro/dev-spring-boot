package com.matiasae.springdemo.mvc.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CourseCodeConstraintValidator implements ConstraintValidator<CourseCode, String> {
    private String coursePrefix;

    @Override
    public void initialize(CourseCode coursePrefix) {
        this.coursePrefix = coursePrefix.value();
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        boolean result = true;
        if (code != null) {
            result = code.startsWith(coursePrefix);
        }
        return result;
    }
}
