package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import lib.Assertion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthTest extends BaseTestCase {
    String cookie, header,
            tokenHeaderName = "x-csrf-token",
            authCookieName = "auth_sid",
            userIdKey = "user_id";
    int userAuthID;

    @BeforeEach
    public void loginUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/ajax/api/user/login")
                .andReturn();

        cookie = getCookies(responseGetAuth,authCookieName);//responseGetAuth.getCookie(authCookieName);
        header = getHeader(responseGetAuth,tokenHeaderName);//responseGetAuth.getHeader(tokenHeaderName);
        userAuthID = getIntFromJson(responseGetAuth,userIdKey);//responseGetAuth.jsonPath().getInt(userIdKey);
    }

    @Test
    public void testAuthUser() {
        Response responseCheckAuth = RestAssured
                .given()
                .header(tokenHeaderName,header)
                .cookie(authCookieName,cookie)
                .get("https://playground.learnqa.ru/ajax/api/user/auth")
                .andReturn();
        Assertion.assertJsonByName(responseCheckAuth,userIdKey,userAuthID,"Unexpected user ID");
    }
    @ParameterizedTest
    @ValueSource(strings = {"cookie","headers"})
    public void testNegativeAuth(String condition) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/ajax/api/user/auth");

        if (condition.equals("cookie")) {
            spec.cookie(authCookieName,cookie);
        }
        else if (condition.equals("headers")) {
            spec.header(tokenHeaderName,header);
        }
        else {
            throw new IllegalArgumentException("Condition value is unknown " + condition);
        }

        Response responseForCheck = spec.get().andReturn();
        Assertion.assertJsonByName(responseForCheck,userIdKey,0,
                "User ID should be 0 for unauth request");
    }

}
