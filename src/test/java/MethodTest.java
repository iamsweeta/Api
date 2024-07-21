import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static groovyjarjarantlr4.v4.runtime.misc.Utils.readFile;
import static io.restassured.RestAssured.given;
@FixMethodOrder(MethodSorters.DEFAULT)

public class MethodTest extends BaseTest {
    String requestBody = null;
    String photoId = "12345";
    String fakePhotoId = "54321";
    String gid = "gid";
    String newDescription = "description";
    String noDescription = "";

    {
        String initialBody = null;
        try {
            initialBody = new String(readFile("src/test/resources/information"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("photo_id", photoId);
        jsonObject.put("gid", gid);
        jsonObject.put("description", newDescription);

        requestBody = jsonObject.toString();
    }

    @Test
    @DisplayName("Успешное создание нового описания фотографии")
    public void testSuccessfulEditDescription() {
        JSONObject jsonObject = new JSONObject(requestBody);
        given(specification)
                .basePath("/photos/edit")
                .body(jsonObject)
                .post()
                .then().log().all()
                .spec(getEditPhotoResponse());

        System.out.println("Описание фото " + photoId + " успешно изменено!" + "\n");
    }

    @Test
    @DisplayName("Удаление описания фотографии")
    public void testEditDescriptionWithoutNewValue(){
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("description", noDescription);
        given()
                .basePath("/photos/edit")
                .body(requestBody)
                .post()
                .then().log().all()
                .spec(getEditPhotoResponse());

        System.out.println("Описание фотографии " + photoId + " успешно удалено!" + "\n");
    }
    @Test
    @DisplayName("Добавление описания к несуществующей фотографии")
    public void testEditDescriptionWithNonExistentPhotoId() {
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("photo_id", fakePhotoId);
        given(specification)
                .basePath("/photos/edit")
                .body(requestBody)
                .post()
                .then().log().all()
                .spec(getEditFakePhotoResponse());
        System.out.println("Фотография " + photoId + " не найдена!" + "\n");

    }

    @Test
    @DisplayName("Добавление описания к фотографии без PhotoID")
    public void testEditDescriptionWithoutPhotoId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", newDescription);
        given(specification)
                .basePath("/photos/edit")
                .body(requestBody)
                .post()
                .then().log().all()
                .spec(getEditNoPhotoIdResponse());
        System.out.println("Photo_id является обязательным параметром" + "\n");
    }
}
