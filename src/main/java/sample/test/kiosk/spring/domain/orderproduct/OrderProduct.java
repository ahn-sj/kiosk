package sample.test.kiosk.spring.domain.orderproduct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.test.kiosk.spring.domain.BaseEntity;
import sample.test.kiosk.spring.domain.order.Order;
import sample.test.kiosk.spring.domain.product.Product;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public OrderProduct(final Order order, final Product product) {
        this.order = order;
        this.product = product;
    }
}
