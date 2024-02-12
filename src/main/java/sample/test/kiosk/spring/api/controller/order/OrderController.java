package sample.test.kiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.test.kiosk.spring.api.ApiResponse;
import sample.test.kiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.test.kiosk.spring.api.service.order.OrderService;
import sample.test.kiosk.spring.api.service.order.response.OrderResponse;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDate = LocalDateTime.now();
        return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), registeredDate));
    }
}
