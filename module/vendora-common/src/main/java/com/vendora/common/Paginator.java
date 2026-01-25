package com.vendora.common;

import java.util.List;

public class Paginator<T> {

    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "page_size";
    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE = 1;

    private final List<T> content;
    private final int page;
    private final int size;
    private final int total;

    private Paginator(Builder<T> builder) {
        this.content = builder.content;
        this.page = builder.page;
        this.size = builder.size;
        this.total = builder.total;
    }

    public List<T> getContent() {
        return this.content;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public int getTotal() {
        return this.total;
    }

    public static class Builder<T> {
        private List<T> content;
        private int page;
        private int size;
        private int total;

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

        public Builder<T> total(int total) {
            this.total = total;
            return this;
        }

        public Paginator<T> build() {
            return new Paginator<>(this);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static int getOffset(int page, int size) {
        return (page - 1) * size;
    }
}
