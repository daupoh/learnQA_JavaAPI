package tests;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelloWorldTest {

    @ParameterizedTest
    @ValueSource (strings = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30" +
                    "|Mobile|No|Android"
            ,"Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1" +
            "|Mobile|Chrome|iOS"
            ,"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)" +
            "|Googlebot|Unknown|Unknown"
            ,"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0" +
            "|Web|Chrome|No"
            ,"Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1" +
            "|Mobile|No|iPhone"} )
    public void testCheckUserAgent(String userAgentCaseSpec) {
        Map <String,String> headers = new HashMap<String, String>();
        String[] caseParams = userAgentCaseSpec.split("\\|");
        String userAgentVal = caseParams[0],
                platform = caseParams[1],
                browser = caseParams[2],
                device = caseParams[3];
        headers.put("User-Agent",userAgentVal);

        Response response = RestAssured
                .given()
                .headers(headers)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();
        System.out.printf("Expected platform %s, browser %s and device %s%n",
                platform,browser,device);
        response.prettyPrint();
        assertEquals(platform,response.jsonPath().getString("platform"),"Platform doesn't have expected value");
        assertEquals(browser,response.jsonPath().getString("browser"),"Browser doesn't have expected value");
        assertEquals(device,response.jsonPath().getString("device"),"Device doesn't have expected value");
    }
    @Test
    public void testCheckHeadersRequest() {
        /*String[] expHeaderNames =
                new String[] {"Date","Content-Type","Content-Length","Connection",
                        "Keep-Alive","Server","x-secret-homework-header","Cache-Control"
                        ,"Expires"};*/
        String expHeaderName="x-secret-homework-header",
                expHeaderVal="Some secret value";
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Headers headers = response.getHeaders();
        /*for (String s:expHeaderNames) {
            assertTrue(headers.hasHeaderWithName(s),"Can't find header "+s);
        }*/
        assertTrue(headers.hasHeaderWithName(expHeaderName));
        assertEquals(expHeaderVal,headers.getValue(expHeaderName),"Non valid header value");
    }
    @Test
    public void testCheckCookieRequest() {
        String expCookieName = "HomeWork",
                expCookieVal = "hw_value";
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String,String> cookies = response.getCookies();
        assertTrue(cookies.containsKey(expCookieName));
        assertEquals(expCookieVal,cookies.get(expCookieName),"Non valid cookie value");
    }
    @ParameterizedTest
    @ValueSource(strings = {"","123","some string","it's a really long string, it surely has more than 15 symbols!"})
    public void testSomeString(String checkingStr) {
        Assertion.assertStrHasLesserLen(checkingStr,15);
    }
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

}
