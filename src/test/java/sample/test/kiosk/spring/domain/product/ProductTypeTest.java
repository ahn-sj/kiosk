package sample.test.kiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType() {

        // given
        ProductType givenType = ProductType.HANDMADE;

        // when
        boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isFalse();

    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStockType2() {

        // given
        ProductType givenType = ProductType.BAKERY;

        // when
        boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isTrue();

    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @ParameterizedTest(name = "{index} ==> 상품 타입: {0}, 재고 관련 타입 여부: {1}")
    @CsvSource({
            "HANDMADE, false",
            "BAKERY, true"
    })
    void containsStockType4(ProductType givenType, boolean expected) {

            // when
            boolean result = ProductType.containsStockType(givenType);

            // then
            assertThat(result).isEqualTo(expected);

    }
}