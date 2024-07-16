import groovy.util.MapEntry;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HelloWorldTest {

    @Test
    public void testCheckRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
    }
    @Test
    public void testCheckJsonParser() {
        JsonPath responseJson = RestAssured
                .given()
                .when()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        System.out.println("All second message item: "+responseJson.getList("messages").get(1));
        System.out.println("Only message text: "+responseJson.getString("messages[1].message"));
    }

    /*@Test
    public void testHelloWorld() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }*/
}
