package com.vendora.common;

import java.util.Collection;
import java.util.List;

public final class EnumUtils {

    private EnumUtils() {}

    public static <E extends Enum<E>> List<String> names(Collection<E> enums) {
        if (enums == null || enums.isEmpty()) {
            return List.of();
        }

        return enums.stream()
          .map(Enum::name)
          .toList();
    }
}
