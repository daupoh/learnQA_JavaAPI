package tests.user;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertion;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {

    @Test
    public void testGetUserDataNotAuth() {
        Response responseGetUser = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertion.assertJsonHasKey(responseGetUser,"username");
        Assertion.assertJsonHasNoKey(responseGetUser,"firstName");
        Assertion.assertJsonHasNoKey(responseGetUser,"lastName");
        Assertion.assertJsonHasNoKey(responseGetUser,"email");
    }
    @Test
    public void testGetUserAsSameUser() {
        Map<String,String> authData = new HashMap<>();
        String[] userDataFields = new String[] {"username","firstName","lastName","email"};
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        String header = this.getHeader(responseAuth,tokenHeaderName),
                cookie = this.getCookies(responseAuth,authCookieName);
        Response responseUserData = RestAssured
                .given()
                .header(tokenHeaderName,header)
                .cookie(authCookieName,cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertion.assertJsonHasKeys(responseUserData,userDataFields);
    }
}
