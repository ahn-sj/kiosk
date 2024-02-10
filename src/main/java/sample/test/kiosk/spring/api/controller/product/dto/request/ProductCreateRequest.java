package sample.test.kiosk.spring.api.controller.product.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.test.kiosk.spring.domain.product.Product;
import sample.test.kiosk.spring.domain.product.ProductSellingStatus;
import sample.test.kiosk.spring.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @Positive(message = "상품 가격은 0보다 커야 합니다.")
    private int price;

    @Builder
    public ProductCreateRequest(final ProductType type, final ProductSellingStatus sellingStatus, final String name, final int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(final String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
