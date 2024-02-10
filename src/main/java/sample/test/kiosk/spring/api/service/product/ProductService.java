package sample.test.kiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.test.kiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
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

    public ProductResponse createProduct(final ProductCreateRequest request) {
        // productNumber: 001 002 003 ...
        // db에서 마지막 저장된 productNumber의 상품 번호를 읽어와서 +1
        final String nextProductNumber = createNextProductNumber();

        final Product product = request.toEntity(nextProductNumber);
        final Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();

        if(latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        final int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }
}
