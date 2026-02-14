package com.vendora.common.exc;

import java.util.Map;

public class ValidationException extends BadRequestException {
    public ValidationException(String message) {
        super(message);
    }
}
