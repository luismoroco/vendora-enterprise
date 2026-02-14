package com.vendora.common;

import com.vendora.common.exc.ValidationException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class ValidatorUtils {
    private ValidatorUtils() {
    }

    public static Mono<Void> string(Object identifier, String value) {
        return !Objects.isNull(value) && !value.isBlank() ? Mono.empty() : Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value)));
    }

    public static Mono<Void> phone(Object identifier, String value) {
        return !PhoneUtils.isValid(value) ? Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value))) : Mono.empty();
    }

    public static Mono<Void> email(Object identifier, String value) {
        return !EmailUtils.isValid(value) ? Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value))) : Mono.empty();
    }

    public static Mono<Void> localDate(Object identifier, String value) {
        return string(identifier, value).then(Mono.defer(() -> {
            try {
                LocalDate.parse(value);
                return Mono.empty();
            } catch (DateTimeParseException var3) {
                return Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value)));
            }
        }));
    }

    public static Mono<Void> nonNull(Object identifier, Object value) {
        return Objects.isNull(value) ? Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, (Object)null))) : Mono.empty();
    }

    public static <T extends Comparable<T>> Mono<Void> numberBetween(Object identifier, T value, T start, T end) {
        return nonNull(identifier, value).then(Mono.defer(() -> value.compareTo(start) >= 0 && value.compareTo(end) <= 0 ? Mono.empty() : Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value)))));
    }

    public static <T extends Comparable<T>> Mono<Void> greaterOrEqualThan(Object identifier, T value, T start) {
        return nonNull(identifier, value).then(Mono.defer(() -> start.compareTo(value) > 0 ? Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value))) : Mono.empty()));
    }

    public static <E extends Enum<E>> Mono<Void> enumValueOf(Object identifier, String value, Class<E> enumType) {
        return string(identifier, value).then(Mono.defer(() -> {
            try {
                Enum.valueOf(enumType, value);
                return Mono.empty();
            } catch (IllegalArgumentException var4) {
                return Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value)));
            }
        }));
    }

    public static <E extends Enum<E>> Mono<Void> enumsValueOf(Object identifier, List<String> values, Class<E> enumType) {
        return nonNull(identifier, values).then(Mono.defer(() -> {
            for(String value : values) {
                try {
                    Enum.valueOf(enumType, value);
                } catch (IllegalArgumentException var6) {
                    return Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value)));
                }
            }

            return Mono.empty();
        }));
    }

    public static Mono<Void> uri(Object identifier, String value) {
        return string(identifier, value).then(Mono.defer(() -> {
            try {
                new URI(value);
                return Mono.empty();
            } catch (URISyntaxException var3) {
                return Mono.error(new ValidationException(LogCatalog.INVALID_PARAMETER.of(identifier, value)));
            }
        }));
    }
}
