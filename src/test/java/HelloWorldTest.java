import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testCheckPasswordHack() {
        String[] passwords= new String[] {"password","123456","123456789","12345678","12345",
                "qwerty","abc123","football","1234567","monkey","111111","letmein","1234",
                "1234567890","dragon","baseball","sunshine","iloveyou","trustno1","princess",
                "adobe123[a]","123123","welcome","login","admin","qwerty123","solo","1q2w3e4r",
                "master","666666","photoshop[a]","1qaz2wsx","qwertyuiop","ashley","mustang","121212",
                "starwars","654321","bailey","access","flower","555555","passw0rd","shadow","lovely",
                "7777777","michael","!@#$%^&*","jesus","password1","superman","hello","charlie","888888",
                "696969","hottie","freedom","aa123456","qazwsx","ninja","azerty","loveme","whatever",
                "donald","batman","zaq1zaq1","Football","000000","123qwe",};
        Map<String,String> body = new HashMap<String,String>();
        body.put("login","super_admin");

        Response authResponse,authCheckResponse;
        String authCookie = "", htmlResult="You are NOT authorized";
        int index = -1;

        while (htmlResult.equals("You are NOT authorized") && index<passwords.length) {
            index++;
            body.put("password",passwords[index]);
            authResponse = RestAssured
                    .given()
                    .when()
                    .body(body)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            authCookie = authResponse.getCookie("auth_cookie");
            if (authCookie != null && !authCookie.isEmpty()) {
                authCheckResponse = RestAssured
                        .given()
                        .cookie("auth_cookie", authCookie)
                        .when()
                        .body(body)
                        .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                        .andReturn();
                htmlResult = authCheckResponse.htmlPath().get("html").toString();
            } else {
                System.out.println("Haven't gotten auth cookie");
            }
        }
        if (!htmlResult.equals("You are NOT authorized")) {
            System.out.println(htmlResult+
                    "\nCorrect password is: "+passwords[index]);
        }


    }
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
