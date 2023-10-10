package sample.test.kiosk.unit;


import sample.test.kiosk.unit.beverage.Americano;
import sample.test.kiosk.unit.beverage.Latte;

public class CafeKioskRunner {
    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println(">>> 아메리카노 추가");

        cafeKiosk.add(new Latte());
        System.out.println(">>> 라떼 추가");

        int price = cafeKiosk.calculateTotalPrice();
        System.out.println("총 주문 가격 = " + price);

    }

}
