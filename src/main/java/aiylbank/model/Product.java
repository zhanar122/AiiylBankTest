package aiylbank.model;

import lombok.Getter;
import lombok.Setter;

public class Product {
    private Integer id;
    @Getter
    private final String name;
    @Setter
    @Getter
    private double price;


    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }  // Добавили сеттер

}