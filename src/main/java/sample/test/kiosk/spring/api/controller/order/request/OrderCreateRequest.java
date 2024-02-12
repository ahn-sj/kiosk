package sample.test.kiosk.spring.api.controller.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.test.kiosk.spring.api.service.order.OrderCreateServiceRequest;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품 번호 리스트는 필수입니다.")
    private List<String> productNumbers;

    @Builder
    private OrderCreateRequest(final List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest.builder()
                .productNumbers(productNumbers)
                .build();
    }
}
