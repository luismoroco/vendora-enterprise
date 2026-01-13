package com.vendora.backend.common.web.api;

import java.util.List;

public class Paginator<T> {

  private final List<T> content;
  private final int page;
  private final int size;

  private Paginator(Builder<T> builder) {
    this.content = builder.content;
    this.page = builder.page;
    this.size = builder.size;
  }

  public List<T> getContent() {
    return content;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }

  public static class Builder<T> {
    private List<T> content;
    private int page;
    private int size;

    public Builder<T> content(List<T> content) {
      this.content = content;
      return this;
    }

    public Builder<T> page(int page) {
      this.page = page;
      return this;
    }

    public Builder<T> size(int size) {
      this.size = size;
      return this;
    }

    public Paginator<T> build() {
      return new Paginator<>(this);
    }
  }

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }
}