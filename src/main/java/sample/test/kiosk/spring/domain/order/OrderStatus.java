package sample.test.kiosk.spring.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    INIT("주문 생성"),
    CANCELD("주문 취소"),
    PAYMENT_COMPLETED("결제 완료"),
    PAYMENT_FAILED("결제 실패"),
    RECEIVED("주문 접수"),
    COMPLETED("처리 완료")
    ;

    private final String text;

}
