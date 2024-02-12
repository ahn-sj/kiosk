package sample.test.kiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.test.kiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.test.kiosk.spring.api.service.order.response.OrderResponse;
import sample.test.kiosk.spring.domain.order.OrderRepository;
import sample.test.kiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.test.kiosk.spring.domain.product.Product;
import sample.test.kiosk.spring.domain.product.ProductRepository;
import sample.test.kiosk.spring.domain.product.ProductSellingStatus;
import sample.test.kiosk.spring.domain.product.ProductType;
import sample.test.kiosk.spring.domain.stock.Stock;
import sample.test.kiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.test.kiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.test.kiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@SpringBootTest
//@Transactional
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(1000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(ProductSellingStatus.HOLD)
                .name("카페라떼")
                .price(3000)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(ProductSellingStatus.STOP_SELLING)
                .name("팥빙수")
                .price(5000)
                .build();
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 1000),
                        tuple("002", "카페라떼", 3000)
                );
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder().productNumbers(List.of("001", "002")).build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(now, 4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000)
                );
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {

        // given
        LocalDateTime now = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder().productNumbers(List.of("001", "001")).build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(now, 2000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );

    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = OrderCreateRequest.builder().productNumbers(List.of("001", "001", "002", "003")).build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(now, 10000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 5000)
                );
        List<Stock> stocks = stockRepository.findAll();
        Assertions.assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stock1.deductQuantity(1); // 테스트 하고자 하는 대상은 주문 생성인데 재고 감소라는 행위가 추가되면 맥락을 이해하기 어려워진다.
        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.createOrder(request.toServiceRequest(), now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock2() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY, "002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 1);

        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = OrderCreateRequest.builder().productNumbers(List.of("001", "001", "002", "003")).build();

        // when & then
        Assertions.assertThatThrownBy(() -> orderService.createOrder(request.toServiceRequest(), now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}