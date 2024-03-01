package sample.test.kiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import sample.test.kiosk.spring.client.mail.MailSendClient;
import sample.test.kiosk.spring.domain.history.mail.MailSendHistory;
import sample.test.kiosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.test.kiosk.spring.domain.order.Order;
import sample.test.kiosk.spring.domain.order.OrderRepository;
import sample.test.kiosk.spring.domain.order.OrderStatus;
import sample.test.kiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.test.kiosk.spring.domain.product.Product;
import sample.test.kiosk.spring.domain.product.ProductRepository;
import sample.test.kiosk.spring.domain.product.ProductSellingStatus;
import sample.test.kiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("결제 완료된 주문들을 조회하여 총 매출 합계를 계산하여 매출 통계 메일을 전송한다.")
    void sendOrderStatisticsMail() {
        // given:
        final LocalDateTime now = LocalDateTime.of(2024, 12, 16, 0, 0);

        final Product product1 = createProduct("001", ProductType.HANDMADE, 1000);
        final Product product2 = createProduct("002", ProductType.HANDMADE, 3000);
        final Product product3 = createProduct("003", ProductType.HANDMADE, 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        final List<Product> products = List.of(product1, product2, product3);
        final Order order1 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 12, 15, 23, 59, 59));
        final Order order2 = createPaymentCompleteOrder(products, now);
        final Order order3 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 12, 15, 23, 59, 59));
        final Order order4 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 12, 17, 0, 0, 0));

        // stubbing: mock 객체에 원하는 행위를 정의하는 것
        Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when:
        final boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2024, 12, 16), "test@test.kr");

        // then:
        assertThat(result).isTrue();

        final List<MailSendHistory> histories = mailSendHistoryRepository.findAll();

        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 9000원입니다");

    }

    private Order createPaymentCompleteOrder(final List<Product> products, final LocalDateTime now) {
        final Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(String productNumber, ProductType productType, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("메뉴 이름")
                .price(price)
                .build();
    }
}