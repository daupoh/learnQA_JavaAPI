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
    public void testCheckTokenJob() {
        JsonPath responseJSON = RestAssured
                .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        int seconds = responseJSON.get("seconds");
        String token = responseJSON.getString("token");
        System.out.println("Token: "+token
                +"\nSeconds to comply:"+ seconds);
        responseJSON = RestAssured
                .given()
                .queryParam("token",token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String status = responseJSON.get("status"),
                error = responseJSON.get("error"),
                result = responseJSON.get("result");
        if (status.equals("Job is NOT ready")
            && error==null
            && result==null) {

            System.out.println("Job sucessfully started");
                try {
                    Thread.sleep(seconds * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                responseJSON = RestAssured
                        .given()
                        .queryParam("token", token)
                        .when()
                        .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                        .jsonPath();
                status = responseJSON.get("status");
                error = responseJSON.get("error");
                result = responseJSON.get("result");
                if (status.equals("Job is ready")
                    && error==null
                    && result!=null && !result.isEmpty()
                )
                    System.out.println("Job result is: "+result);
            }
        else
            System.out.println("Job can't be started");
    }
    @Test
    public void testCheckLongRedirect() {
        Response response = RestAssured
                .given()
                    .redirects()
                    .follow(false)
                .when()
                    .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        int statusCode = response.statusCode(),
            counter=0;
        String newLocation="";
        while (statusCode!=200) {
            newLocation = response.getHeader("Location");
            counter++;
            response = RestAssured
                    .given()
                        .redirects()
                        .follow(false)
                    .when()
                        .get(newLocation)
                    .andReturn();
            statusCode = response.statusCode();
        }
        System.out.println("Final URL: "+newLocation+
                "\nStatus code: "+statusCode +
                "\nRedirects count: "+counter);
    }
    @Test
    public void testCheckRedirect() {
        Response response = RestAssured
                .given()
                    .redirects()
                    .follow(false)
                .when()
                    .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        System.out.println("Redirect URL: "+response.getHeader("Location")+
                "\nStatus code: "+response.getStatusCode());
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
