package sample.test.kiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.test.kiosk.spring.api.service.product.response.ProductResponse;
import sample.test.kiosk.spring.domain.product.Product;
import sample.test.kiosk.spring.domain.product.ProductRepository;
import sample.test.kiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }
}
