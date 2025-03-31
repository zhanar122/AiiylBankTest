package aiylbanktest;

import aiylbank.AiylBankApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;;

@SpringBootTest(
        classes = AiylBankApplication.class, // Указываем главный класс приложения
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ProductServiceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void testCreateProductSuccess() {
        given()
                .contentType("application/json")
                .body("{ \"id\": 1, \"name\": \"Valid Product\", \"price\": 500.0 }")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    void testEvenProductIdNotAvailable() {
        given()
                .contentType("application/json")
                .body("{ \"id\": 2, \"name\": \"Product A\", \"price\": 100.0 }")
                .when()
                .post("/api/products")
                .then()
                .statusCode(403)
                .body(equalTo("Products with even IDs are not available."));
    }

    @Test
    void testPrimeNumberIdRequiresSpecialAccess() {
        given()
                .contentType("application/json")
                .body("{ \"id\": 7, \"name\": \"Prime Product\", \"price\": 100.0 }")
                .when()
                .post("/api/products")
                .then()
                .statusCode(403)
                .body(equalTo("Products with prime IDs require special access."));
    }

    @Test
    void testProductPriceOver1000RequiresApproval() {
        given()
                .contentType("application/json")
                .body("{ \"id\": 9, \"name\": \"Expensive Product\", \"price\": 1500.0 }")
                .when()
                .post("/api/products")
                .then()
                .statusCode(403)
                .body(equalTo("Products over $1000 require special approval."));
    }

    @Test
    void testCannotDeleteProductsOver100Dollars() {
        // ID=9 (не простое, не четное, не делится на 3)
        given()
                .contentType("application/json")
                .body("{ \"id\": 9, \"name\": \"Test Product\", \"price\": 150.0 }")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200);

        given()
                .when()
                .delete("/api/products/9")
                .then()
                .statusCode(403)
                .body(equalTo("Cannot delete products with price over $100."));
    }

    @Test
    void testPriceChangeCannotExceed500Dollars() {
        // ID=25 (не простое, не четное)
        given()
                .contentType("application/json")
                .body("{ \"id\": 25, \"name\": \"Test Product\", \"price\": 200.0 }")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200);

        given()
                .contentType("application/json")
                .body("{ \"id\": 25, \"name\": \"Test Product\", \"price\": 800.0 }")
                .when()
                .put("/api/products/25")
                .then()
                .statusCode(403)
                .body(equalTo("Price change cannot exceed $500."));
    }

}