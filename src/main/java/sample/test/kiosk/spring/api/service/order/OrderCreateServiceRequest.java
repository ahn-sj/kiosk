package sample.test.kiosk.spring.api.service.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    private List<String> productNumbers;

    @Builder
    private OrderCreateServiceRequest(final List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

}
