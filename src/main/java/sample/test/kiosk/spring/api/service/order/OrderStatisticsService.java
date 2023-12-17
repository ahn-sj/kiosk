package sample.test.kiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.test.kiosk.spring.api.service.mail.MailService;
import sample.test.kiosk.spring.domain.order.Order;
import sample.test.kiosk.spring.domain.order.OrderRepository;
import sample.test.kiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public void sendOrderStatisticsMail(LocalDate orderDate, String email) {
        // 해당 일자에 결제 완료된 주문들을 가져와서
        LocalDateTime startDateTime = orderDate.atStartOfDay();                     // 2023-12-16 -> 2023-12-16 00:00
        LocalDateTime endDateTime = orderDate.plusDays(1).atStartOfDay(); // 2023-12-16 -> 2023-12-17 00:00
        OrderStatus completed = OrderStatus.COMPLETED;

        List<Order> orders = orderRepository.findOrdersBy(startDateTime, endDateTime, completed);

        // 총 매출 합계를 계산
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        // 메일 전송
        boolean result = mailService.sendMail(
                "no-reply@kiosk.com",
                email,
                String.format("[매출통계] %s", orderDate),
                String.format("총 매출 합계는 %s원입니다", totalAmount)
        );

        if(!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }
    }
}
