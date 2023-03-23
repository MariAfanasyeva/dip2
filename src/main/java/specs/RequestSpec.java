package specs;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class RequestSpec {
    public RequestSpecification baseSpec() throws InterruptedException {
        Thread.sleep(600);
        return given().log().all()
                .contentType(ContentType.JSON);
    }
}

