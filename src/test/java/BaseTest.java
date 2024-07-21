import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;

public class BaseTest {
    //В данном родительском классе содержится базовая спецификация и спецификации к методам тестовых классов
    private final String BASE_URI = "https://api.ok.ru/";
    RequestSpecification specification = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json;;charset=UTF-8")
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .build();
    protected ResponseSpecification getAssertionSpecification() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        return builder.build();
    }

    protected ResponseSpecification getError404AssertionSpecification() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(404);
        return builder.build();
    }
    protected ResponseSpecification getError403AssertionSpecification() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(403);
        return builder.build();
    }
    protected ResponseSpecification getEditPhotoResponse(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpecification())
                .expectBody("status", equalTo(true));
        return builder.build();
    }

    protected ResponseSpecification getEditFakePhotoResponse(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getError404AssertionSpecification())
                .expectBody("status", equalTo(false));
        return builder.build();
    }

    protected ResponseSpecification getEditNoPhotoIdResponse(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getError403AssertionSpecification())
                .expectBody("status", equalTo(false));
        return builder.build();
    }
}