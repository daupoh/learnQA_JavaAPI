import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;

public class HelloWorldTest {
    @Test
    public void testHelloMyName() {
        System.out.println("Hello from Eugene Stoyalov");
    }

    /*@Test
    public void testHelloWorld() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }*/
}
