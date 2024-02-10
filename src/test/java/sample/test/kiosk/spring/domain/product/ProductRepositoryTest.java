package sample.test.kiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.test.kiosk.spring.domain.product.ProductSellingStatus.*;

@ActiveProfiles("test")
@SpringBootTest
//@DataJpaTest // JPA 관련된 설정 위주로 되어있다.
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findALlBySellingStatusIn() {

        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", ProductType.HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, ProductSellingStatus.HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품 번호를 읽어온다.")
    void findLatestProduct() {

        final String targetProductNumber = "003";

        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct(targetProductNumber, ProductType.HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        final String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        Assertions.assertThat(latestProductNumber).isEqualTo(targetProductNumber);
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

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품 번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    void findLatestProductNumberWhenProductIsEmpty() {
        // when
        final String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        Assertions.assertThat(latestProductNumber).isNull();
    }
}