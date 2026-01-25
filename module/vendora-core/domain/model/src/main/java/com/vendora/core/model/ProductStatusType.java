package com.vendora.core.model;

import lombok.Getter;

@Getter
public enum ProductStatusType {
    ENABLED(1),
    DISABLED(2);

    private final int value;

    private ProductStatusType(int value) {
        this.value = value;
    }
}
