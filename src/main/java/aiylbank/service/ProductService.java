package aiylbank.service;

import aiylbank.model.Product;
import aiylbank.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        if (product.getId() % 2 == 0) {
            throw new RuntimeException("Products with even IDs are not available.");
        }
        if (isPrime(product.getId())) {
            throw new RuntimeException("Products with prime IDs require special access.");
        }
        if (product.getPrice() > 1000) {
            throw new RuntimeException("Products over $1000 require special approval.");
        }
        return productRepository.save(product);
    }

    public Product getProductById(int id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        return product;
    }

    public Product update(int id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found.");
        }
        if (Math.abs(existingProduct.getPrice() - updatedProduct.getPrice()) > 500) {
            throw new RuntimeException("Price change cannot exceed $500.");
        }
        updatedProduct.setId(id); // Убедимся, что ID не меняется
        return productRepository.save(updatedProduct);
    }

    public void deleteProduct(int id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new RuntimeException("Product not found.");
        }
        if (product.getPrice() > 100) {
            throw new RuntimeException("Cannot delete products with price over $100.");
        }
        productRepository.deleteById(id);
    }

    private boolean isPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }
}