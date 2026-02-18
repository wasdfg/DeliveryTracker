package com.example.deliverytracker.store.dto;

import com.example.deliverytracker.store.entity.Option;
import com.example.deliverytracker.store.entity.OptionGroup;
import com.example.deliverytracker.store.entity.Product;
import com.example.deliverytracker.store.entity.ProductCategory;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponse {

    private Long productId;
    private String name;
    private BigDecimal price;
    private ProductCategory category;
    private String description;
    private int stock;
    private String imageUrl;

    private List<OptionGroupResponse> optionGroups;

    public ProductResponse(Product product) {
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.description = product.getDescription();
        this.stock = product.getStock();
        this.imageUrl = product.getImageUrl();

        this.optionGroups = product.getOptionGroups().stream()
                .map(OptionGroupResponse::new)
                .collect(Collectors.toList());
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product);
    }

    @Getter
    public static class OptionGroupResponse {
        private Long optionGroupId;
        private String name;
        private boolean isRequired;
        private List<OptionItemResponse> options;

        public OptionGroupResponse(OptionGroup group) {
            this.optionGroupId = group.getId();
            this.name = group.getName();
            this.isRequired = group.isRequired();
            this.options = group.getOptions().stream()
                    .map(OptionItemResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    public static class OptionItemResponse {
        private Long optionId;
        private String name;
        private BigDecimal additionalPrice;

        public OptionItemResponse(Option option) {
            this.optionId = option.getId();
            this.name = option.getName();
            this.additionalPrice = option.getAdditionalPrice();
        }
    }
}
