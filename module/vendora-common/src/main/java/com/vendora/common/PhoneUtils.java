package com.vendora.common;

import java.util.Objects;

public class PhoneUtils {
    private static final String PHONE_REGEX = "^\\+?[0-9]{7,15}$";

    private PhoneUtils() {
    }

    public static boolean isValid(String phoneNumber) {
        return Objects.nonNull(phoneNumber) && phoneNumber.matches("^\\+?[0-9]{7,15}$");
    }
}
