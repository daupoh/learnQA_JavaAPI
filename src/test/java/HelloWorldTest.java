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
    public void testCheckAuthCookies() {
        Map<String, String> body = new HashMap<>();
        body.put("login","secret_login");
        body.put("password","secret_pass");
        JsonPath responseJson = RestAssured
                .given()
                .body(body)/*
                .queryParam("param1","value1")
                .queryParam("param2","value2")*/
                /*.body("param1=value1&param2=value2")*/
                .when()
                .post("https://playground.learnqa.ru/api/get_json_homework")
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
