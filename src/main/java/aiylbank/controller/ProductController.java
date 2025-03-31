package aiylbank.controller;

import aiylbank.model.Product;
import aiylbank.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Создание нового продукта
     * @param product Данные продукта из тела запроса
     * @return ResponseEntity с результатом операции
     */

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        log.info("Создание продукта: ID={}, Name={}", product.getId(), product.getName());

        try {
            System.out.println("Trying to save product: " + product);
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(savedProduct);
        } catch (RuntimeException e) {
            System.out.println("Business error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            // Если продукт не найден - 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Product updatedProduct) {
        try {
            Product updated = productService.update(id, updatedProduct);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            // Для ошибок валидации возвращаем 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}