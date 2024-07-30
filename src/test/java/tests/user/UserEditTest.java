package tests.user;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertion;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    @Test
    public void testEditJustCreatedTest() {
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreatedAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .jsonPath();
        String userId = responseCreatedAuth.getString("id");

        Map<String,String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String newName = "changedName";
        Map<String, String> editedUser = new HashMap<>();
        editedUser.put("firstName",newName);

        Response responseEditUser = RestAssured
                .given()
                .header(tokenHeaderName,getHeader(responseGetAuth,tokenHeaderName))
                .cookie(authCookieName,getCookies(responseGetAuth,authCookieName))
                .body(editedUser)
                .put("https://playground.learnqa.ru/api/user/"+userId)
                .andReturn();

        Response responseUserData = RestAssured
                .given()
                .header(tokenHeaderName,getHeader(responseGetAuth,tokenHeaderName))
                .cookie(authCookieName,getCookies(responseGetAuth,authCookieName))
                .get("https://playground.learnqa.ru/api/user/"+userId)
                .andReturn();
        Assertion.assertJsonStringByName(responseUserData,"firstName",newName);
    }
}
