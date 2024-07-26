package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertion;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    @Test
    public void testCreateUserSuccesfully () {
        Map<String,String> userData = new HashMap<>();
        String email = DataGenerator.getRandomEmail();

        userData.put("email",email);
        userData.put("password","123");
        userData.put("username","learnqa");
        userData.put("firstName","learnqa");
        userData.put("lastName","learnqa");

        Response responseCreateUser = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/ajax/api/user/")
                .andReturn();
        //Assertion.assertResponseTextEquals(responseCreateUser,String.format("Users with email '%s' already exists",email));
        Assertion.assertResponseCodeResult(responseCreateUser,200);
        Assertion.assertJsonHasKey(responseCreateUser,"id");
    }
    @Test
    public void testCreateUserWithExistingEmail () {
        Map<String,String> userData = new HashMap<>();
        String email = "vinkotov@example.com";

        userData.put("email",email);
        userData.put("password","123");
        userData.put("username","learnqa");
        userData.put("firstName","learnqa");
        userData.put("lastName","learnqa");

        Response responseCreateUser = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/ajax/api/user/")
                .andReturn();
        Assertion.assertResponseTextEquals(responseCreateUser,String.format("Users with email '%s' already exists",email));
        Assertion.assertResponseCodeResult(responseCreateUser,400);
    }
}
