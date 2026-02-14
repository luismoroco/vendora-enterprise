package com.vendora.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductCategory {

    public static final String TYPE = "ProductCategory";

    public static final String PRODUCT_CATEGORY_ID = "productCategoryId";
    public static final String NAME = "name";
    public static final String FEATURED = "featured";
    public static final String IMAGE_URL = "imageUrl";
    public static final String TENANT_ID = "tenantId";

    private Long    productCategoryId;
    private String  name;
    private Boolean featured;
    private String  imageUrl;
    private Long    tenantId;
}
