package com.vendora.common;

public enum LogCatalog {
    ENTITY_NOT_FOUND {
        @Override
        public String of(Object identifier) {
            return String.format("%s not found", identifier);
        }

        @Override
        public String of(Object identifier, Object value) {
            return String.format("%s %s not found", value, identifier);
        }
    },
    INVALID_PARAMETER {
        @Override
        public String of(Object identifier) {
            return String.format("Invalid parameter: %s", identifier);
        }

        @Override
        public String of(Object identifier, Object value) {
            return String.format("%s %s invalid parameter", value, identifier);
        }
    },
    ENTITY_ALREADY_EXISTS {
        @Override
        public String of(Object identifier) {
            return String.format("%s already exists", identifier);
        }

        @Override
        public String of(Object identifier, Object value) {
            return String.format("%s %s already exists", value, identifier);
        }
    },
    BEGIN_PROCESSING {
        @Override
        public String of(Object identifier) {
            return String.format("Begin Processing: %s", identifier);
        }

        @Override
        public String of(Object identifier, Object value) {
            return String.format("Begin Processing: %s with %s", identifier, value);
        }
    },
    ERROR_PROCESSING {
        @Override
        public String of(Object identifier) {
            return String.format("Error Processing: %s", identifier);
        }

        @Override
        public String of(Object identifier, Object value) {
            return String.format("Error Processing: %s with %s", identifier, value);
        }
    },
    ENTITY_FOUND {
        @Override
        public String of(Object identifier) {
            return String.format("%s found", identifier);
        }

        @Override
        public String of(Object identifier, Object value) {
            return String.format("%s %s found", identifier, value);
        }
    },
    SUCCESSFUL_PROCESSING {
        @Override
        public String of(Object identifier) {
            return String.format("Successful Processing: %s", identifier);
        }

        @Override
        public String of(Object identifier, Object value) {
            return String.format("Successful Processing: %s with %s", identifier, value);
        }
    };

    public abstract String of(Object identifier);

    public abstract String of(Object identifier, Object value);
}

