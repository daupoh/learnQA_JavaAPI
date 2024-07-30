package tests.user;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
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
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
@Epic("Authorization cases")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {
    String cookie, header,
            userIdKey = "user_id";
    int userAuthID;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = apiCoreRequests.
                makePostRequest("https://playground.learnqa.ru/ajax/api/user/login",authData);

        cookie = getCookies(responseGetAuth,authCookieName);//responseGetAuth.getCookie(authCookieName);
        header = getHeader(responseGetAuth,tokenHeaderName);//responseGetAuth.getHeader(tokenHeaderName);
        userAuthID = getIntFromJson(responseGetAuth,userIdKey);//responseGetAuth.jsonPath().getInt(userIdKey);
    }

    @Test
    @Description("This test succesfully authorize user by email and password")
    @DisplayName("Test Pos Auth User")
    public void testAuthUser() {
        Response responseCheckAuth =
                apiCoreRequests.makeGetRequestWithCookie("https://playground.learnqa.ru/ajax/api/user/auth",
                        header,cookie);
        Assertion.assertJsonIntByName(responseCheckAuth,userIdKey,userAuthID,"Unexpected user ID");
    }

    @Description("This test checks authorize w/o cookie or header")
    @DisplayName("Test Neg Auth User")
    @ParameterizedTest
    @ValueSource(strings = {"cookie","headers"})
    public void testNegativeAuth(String condition) {
        Response responseAuth;
        String baseUri="https://playground.learnqa.ru/ajax/api/user/auth";

        if (condition.equals("cookie")) {
            responseAuth = apiCoreRequests.makeGetRequestWithCookie(baseUri,cookie);
        }
        else if (condition.equals("headers")) {
           responseAuth = apiCoreRequests.makeGetRequestWithToken(baseUri,header);
        }
        else {
            throw new IllegalArgumentException("Condition value is unknown " + condition);
        }
        Assertion.assertJsonIntByName(responseAuth,userIdKey,0,
                "User ID should be 0 for unauth request");
    }

}
