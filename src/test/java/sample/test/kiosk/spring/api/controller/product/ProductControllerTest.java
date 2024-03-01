package sample.test.kiosk.spring.api.controller.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import sample.test.kiosk.spring.ControllerTestSupport;
import sample.test.kiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.test.kiosk.spring.api.service.product.response.ProductResponse;
import sample.test.kiosk.spring.domain.product.ProductSellingStatus;
import sample.test.kiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTestSupport {

    // lib: mockito - mocking framework
    // - @Mock
    // - @MockBean

    @Test
    @DisplayName("신규 상품을 등록한다.")
    void createProduct() throws Exception {

        // restAssured
        final ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        mockMvc.perform(
                post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
            )
                .andDo(print()) // print log
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수 값이다.")
    void createProductWithoutType() throws Exception {

        // restAssured
        final ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) // print log
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 판매 상태는 필수 값이다.")
    void createProductWithoutSellingStatus() throws Exception {

        // restAssured
        final ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();

        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) // print log
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 이름은 필수 값이다.")
    void createProductWithoutName() throws Exception {

        // restAssured
        final ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .price(4000)
                .build();

        mockMvc.perform(
                        post("/api/v1/products/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) // print log
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 가격은 0보다 커야 합니다.")
    void createProductWithoutZeroPrice() throws Exception {

        // restAssured
        final ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        mockMvc.perform(
                        get("/api/v1/products/selling")
//                                .queryParam("name", "이름")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) // print log
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 0보다 커야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("판매 상품을 조회한다.")
    void getSellingProducts() throws Exception {

        final List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        mockMvc.perform(
                        get("/api/v1/products/selling")

                )
                .andDo(print()) // print log
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

}