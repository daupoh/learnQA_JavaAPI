package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequestWithCookie(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(BaseTestCase.tokenHeaderName,token)
                .cookie(BaseTestCase.authCookieName,cookie)
                .get(url)
                .andReturn();
    }
    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie(BaseTestCase.authCookieName,cookie)
                .get(url)
                .andReturn();
    }
    @Step("Make a GET-request with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(BaseTestCase.tokenHeaderName,token)
                .get(url)
                .andReturn();
    }
    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String,String> data) {
        return given()
                .filter(new AllureRestAssured())
                .body(data)
                .post(url)
                .andReturn();
    }
}
