package sample.test.kiosk.spring.api.service.product.response;

import lombok.Builder;
import lombok.Getter;
import sample.test.kiosk.spring.domain.product.Product;
import sample.test.kiosk.spring.domain.product.ProductSellingStatus;
import sample.test.kiosk.spring.domain.product.ProductType;

@Getter
public class ProductResponse {

    private Long id;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder
    private ProductResponse(
            final Long id, final String productNumber,
            final ProductType type, final ProductSellingStatus sellingStatus,
            final String name, final int price
    ) {
        this.id = id;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(final Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .type(product.getType())
                .sellingStatus(product.getSellingStatus())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
