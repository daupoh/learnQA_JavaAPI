import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HelloWorldTest {

    @Test
    public void testCheckAuthCookies() {
        Map<String, String> body = new HashMap<>();
        body.put("login","secret_login");
        body.put("password","secret_pass");
        Response response = RestAssured
                .given()
                .body(body)/*
                .queryParam("param1","value1")
                .queryParam("param2","value2")*/
                /*.body("param1=value1&param2=value2")*/
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();
        response.prettyPrint();
        Headers responseHeaders = response.getHeaders();
        Map<String,String> cookies = response.getCookies();
        System.out.println("Headers:\n"+responseHeaders);
        System.out.println("Cookies:\n"+cookies);

    }
    @Test
    public void testCheckTypeStatus() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get(  "https://playground.learnqa.ru/api/get_303")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }

    @Test
    public void testCheckType() {
        Map<String, Object> body = new HashMap<>();
        body.put("param1","value1");
        body.put("param2","value2");
        Response response = RestAssured
                .given()/*
                .queryParam("param1","value1")
                .queryParam("param2","value2")*/
                /*.body("param1=value1&param2=value2")*/
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }
    @Test
    public void testHelloMyName() {
        System.out.println("Hello from Eugene Stoyalov");
    }
    @Test
    public void testSimpleGet() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
    @Test
    public void testRestAssured() {
        Map<String,String> params = new HashMap<>();
        params.put("name","John");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        //response.prettyPrint();
        String answer = response.get("answer2");
        if (answer!=null) {
            System.out.println(answer);
        }
        else {
            System.out.println("The key is absent");
        }
    }
    /*@Test
    public void testHelloWorld() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }*/
}
