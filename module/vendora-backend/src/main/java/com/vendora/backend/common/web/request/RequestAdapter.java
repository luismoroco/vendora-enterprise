package com.vendora.backend.common.web.request;

import com.vendora.backend.common.mapping.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Field;
import java.util.Map;

public interface RequestAdapter<T> {
  static Logger logger = LoggerFactory.getLogger(RequestAdapter.class);

  public default T buildRequest() {
    return Mapper.get().map(this, this.inferTargetClass());
  }

  public default T buildRequest(Map<String, Object> overrideKeys) {
    T result = this.buildRequest();

    overrideKeys.forEach((key, value) -> {
      try {
        Field field = result.getClass().getDeclaredField(key);
        field.setAccessible(true);
        field.set(result, value);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        logger.error("Error while overriding field [fieldName=%s]".formatted(key));
        throw new RuntimeException(e);
      }
    });

    return result;
  }

  @SuppressWarnings("unchecked")
  private Class<T> inferTargetClass() {
    ParameterizedType type = (ParameterizedType) this.getClass().getGenericInterfaces()[0];

    return (Class<T>) type.getActualTypeArguments()[0];
  }
}
