package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected String getHeader(Response response, String name) {
        Headers headers = response.getHeaders();
        assertTrue(headers.hasHeaderWithName(name),"Response hasn't header with name "+name);

        return headers.getValue(name);
    }
    protected String getCookies(Response response, String name) {
        Map<String,String> cookies = response.getCookies();

        assertTrue(cookies.containsKey(name),"Response hasn't cookie with name "+name);

        return cookies.get(name);
    }
}
