package requests;
import deserializator.user.UserResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import serialization.User;
import specs.RequestSpec;
import urls.Endpoints;

//все запросы для работы с апи юзера
public class UserClient extends RequestSpec {
    private final Endpoints endpoints = new Endpoints();
    private String token;
    private String accessToken;

    @Step("Создаем юзера и возвращаем ответ от сервера")
    public Response createUser(User user) throws InterruptedException {
        return baseSpec()
                .baseUri(endpoints.baseUri)
                .body(user)
                .when()
                .post(endpoints.registerUri);
    }

    @Step("Авторизуемся за юзера и возвращаем ответ от сервера")
    public Response loginUser(User user) throws InterruptedException {
        return baseSpec()
                .baseUri(endpoints.baseUri)
                .body(user)
                .when()
                .post(endpoints.loginUri);
    }

    @Step("Установка токена от Response")
    public void setToken(Response response) {
        String tmpToken = response.then().extract().path("accessToken");
        this.accessToken = tmpToken.replace("Bearer ", "");
    }

    @Step("Получение токена")
    public String getAccessToken() {
        return accessToken;
    }

    @Step("Установка токена из десериализации")
    public void setAccessToken(UserResponse userResponse) {
        this.accessToken = userResponse.getAccessToken().replace("Bearer ", "");
    }

    @Step("Удаление юзера")
    public void deleteUser(String token) throws InterruptedException {
        if (token != null) {
            baseSpec()
                    .auth().oauth2(token)
                    .baseUri(endpoints.baseUri)
                    .delete(endpoints.authUser).then().assertThat().statusCode(202);
        }
    }

    @Step("Проверка данных юзера")
    public UserResponse checkUserCreds(User user) throws InterruptedException {
        return baseSpec()
                .baseUri(endpoints.baseUri)
                .body(user)
                .when()
                .post(endpoints.registerUri)
                .as(UserResponse.class);
    }

    @Step("Изменение данных юзера")
    public Response changeUserData(User user, String token) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(token)
                .baseUri(endpoints.baseUri)
                .body(user)
                .when()
                .patch(endpoints.authUser);
    }
}

