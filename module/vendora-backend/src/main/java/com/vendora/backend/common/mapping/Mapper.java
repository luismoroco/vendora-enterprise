package com.vendora.backend.common.mapping;

import org.modelmapper.ModelMapper;

import java.util.Objects;

public class Mapper {
  private final ModelMapper mapper;
  private static Mapper instance;

  public Mapper(ModelMapper mapper) {
    this.mapper = mapper;

    if (Objects.nonNull(instance)) {
      return;
    }
    Mapper.instance = this;
  }

  public static Mapper get() {
    return instance;
  }

  public <S, T> T map(S source, Class<T> targetClass) {
    return mapper.map(source, targetClass);
  }
}