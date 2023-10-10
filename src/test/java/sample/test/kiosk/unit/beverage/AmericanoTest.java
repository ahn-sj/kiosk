package sample.test.kiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AmericanoTest {

    @Test
    void name() {
        Americano americano = new Americano();
//        assertEquals(americano.getName(), "아메리카노");                // junit api
        assertThat(americano.getName()).isEqualTo("아메리카노"); // AssertJ
    }

    @Test
    void getPrice() {
        Americano americano = new Americano();
        assertThat(americano.getPrice()).isEqualTo(4000);

    }

}