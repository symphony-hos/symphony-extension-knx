package io.symphony.knx.properties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class KNXPointConfigValidator implements ConstraintValidator<ValidKNXPointConfig, KNXPointConfigProperties> {
 
    public void initialize(ValidKNXPointConfig constraint) {
    }
 
    public boolean isValid(KNXPointConfigProperties properties, ConstraintValidatorContext context) {
        boolean result = true;
 
        if (properties.getWrite() == null && properties.getRead() == null) {
        	context.buildConstraintViolationWithTemplate("At least one of read or write must be set.");
        }
    	return result;
    }
 
}