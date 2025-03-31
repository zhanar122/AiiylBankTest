package aiylbank.repository;

import aiylbank.model.Product;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    private final Map<Integer, Product> products = new HashMap<>();

    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public Product findById(int id) {
        return products.get(id);
    }

    public void deleteById(int id) {
        products.remove(id);
    }

    public boolean existsById(int id) {
        return products.containsKey(id);
    }
}
