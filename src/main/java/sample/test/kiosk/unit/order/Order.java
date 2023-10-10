package sample.test.kiosk.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.test.kiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {

    private final LocalDateTime orderDateTime; // 주문 일시
    private final List<Beverage> beverages;    // 음료

}
