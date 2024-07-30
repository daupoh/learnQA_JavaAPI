package tests.user;

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
        userData = DataGenerator.getRegistrationData();

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/ajax/api/user/",
                userData
        );
        Assertion.assertResponseCodeResult(responseCreateUser,200);
        Assertion.assertJsonHasKey(responseCreateUser,"id");
    }
    @Test
    public void testCreateUserWithExistingEmail () {
        Map<String,String> userData = new HashMap<>();
        String email = "vinkotov@example.com";
        userData.put("email",email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/ajax/api/user/",
                userData
        );
        Assertion.assertResponseTextEquals(responseCreateUser,String.format("Users with email '%s' already exists",email));
        Assertion.assertResponseCodeResult(responseCreateUser,400);
    }

    @Test
    public void testCreateUserWithIncorrectEmail() {

    }
}
