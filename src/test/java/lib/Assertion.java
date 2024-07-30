package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertion {
    private static final String msgJsonEquals = "JSON value is not equal to expected value",
                        msgResponseTextEquals = "Response text is not as expected",
                        msgResponseCodeEquals = "Response code is not as expected";
    private static String customMessage="";
    private static String getMessage(String baseMsg) {
        return customMessage.isEmpty()?baseMsg:customMessage;
    }
    public static void assertJsonIntByName(Response response, String name, int expectedVal) {
        response.then().assertThat().body("$",hasKey(name));

        int value = response.jsonPath().getInt(name);
        assertEquals(expectedVal,value,getMessage(msgJsonEquals));
    }
    public static void assertResponseTextEquals (Response response, String text) {
        assertEquals(text,response.asString(),getMessage(msgResponseTextEquals));
    }
    public static void assertResponseCodeResult(Response response, int statusCode) {
        assertEquals(statusCode,response.statusCode(),getMessage(msgResponseCodeEquals));
    }
    public static void assertJsonIntByName(Response response, String name, int expectedVal, String message) {
        customMessage = message;
        assertJsonIntByName(response,name,expectedVal);
        customMessage="";
    }
    public static void assertJsonStringByName (Response response, String name, String expectedVal) {
        response.then().assertThat().body("$",hasKey(name));

        String value = response.jsonPath().getString(name);
        assertEquals(expectedVal,value,getMessage(msgJsonEquals));
    }

    public static void assertStrHasLesserLen(String checkingStr, int borderLen) {
        assertTrue(checkingStr.length()>borderLen,"String doesn't have enough symbols!");
    }
    public static void assertJsonHasKey (Response response,String key) {
      response.then().assertThat().body("$",hasKey(key));
    }
    public static void assertJsonHasNoKey (Response response, String key) {
        response.then().assertThat().body("$",not(hasKey(key)));
    }
    public static void assertJsonHasKeys(Response response,String[] keys ) {
        for (String key: keys) {
            assertJsonHasKey(response,key);
        }
    }
}
