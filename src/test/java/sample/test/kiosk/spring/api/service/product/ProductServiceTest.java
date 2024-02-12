package sample.test.kiosk.spring.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.test.kiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.test.kiosk.spring.api.service.product.response.ProductResponse;
import sample.test.kiosk.spring.domain.product.Product;
import sample.test.kiosk.spring.domain.product.ProductRepository;
import sample.test.kiosk.spring.domain.product.ProductSellingStatus;
import sample.test.kiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.tuple;
import static sample.test.kiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.test.kiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품 번호에서 1 증가한 값이다.")
    void createProduct() {

        // given:
        Product product = createProduct("001", ProductType.HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product);

        final ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when:
        productService.createProduct(request.toServiceRequest());

        // then:
        final List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", ProductType.HANDMADE, SELLING, "아메리카노", 4000),
                        tuple("002", ProductType.HANDMADE, STOP_SELLING, "카푸치노", 5000)
                );
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다")
    void createProductWhenProductsIsEmpty() {

        // given:
        final ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when:
        productService.createProduct(request.toServiceRequest());

        // then:
        final List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains(
                        tuple("001", ProductType.HANDMADE, SELLING, "카푸치노", 5000)
                );
    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}