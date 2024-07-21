package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertion {
    public static void assertJsonByName(Response response, String name, int expectedVal) {
        response.then().assertThat().body("$",hasKey(name));

        int value = response.jsonPath().getInt(name);
        assertEquals(expectedVal,value,"JSON value is not equal to expected value");
    }
    public static void assertStrHasLesserLen(String checkingStr, int borderLen) {
        assertTrue(checkingStr.length()>borderLen,"String doesn't have enough symbols!");
    }
}
