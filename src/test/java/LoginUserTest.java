import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import requests.UserClient;
import serialization.User;
import tools.UserDataGenerator;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    private final UserDataGenerator uGen = new UserDataGenerator();
    private final User user = new User(uGen.genEmail(), uGen.genPassword(), uGen.genName());
    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Создание пользователя с авторизацией")
    @Description("Проверяем авторизацию и код ответа")
    public void checkStatusCodeLoginUser() throws InterruptedException {
        userClient.createUser(user);
        user.setName(null);
        Response response = userClient.loginUser(user);
        userClient.setToken(response);
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверной почтой")
    @Description("Проверяем, что авторизация не произошла, вернулось корректное сообщение об ошибке и код ответа")
    public void checkLoginWrongUserEmail() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        user.setName(null);
        user.setEmail(uGen.genEmail());
        Response responseLogin = userClient.loginUser(user);
        responseLogin.then().assertThat().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверяем, что авторизация не произошла, вернулось корректное сообщение об ошибке и код ответа")
    public void checkLoginWrongUserPassword() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        user.setName(null);
        user.setPassword(uGen.genPassword());
        Response responseLogin = userClient.loginUser(user);
        responseLogin.then().assertThat().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя без указания почты")
    @Description("Проверяем, что авторизация не произошла, вернулось корректное сообщение об ошибке и код ответа")
    public void checkLoginWithoutUserEmail() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        user.setName(null);
        user.setEmail(null);
        Response responseLogin = userClient.loginUser(user);
        responseLogin.then().assertThat().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя без указания пароля")
    @Description("Проверяем, что авторизация не произошла, вернулось корректное сообщение об ошибке и код ответа")
    public void checkLoginWithoutUserPassword() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        user.setName(null);
        user.setPassword(null);
        Response responseLogin = userClient.loginUser(user);
        responseLogin.then().assertThat().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    //удаляем юзера после тестирования
    @After
    public void clearUserData() throws InterruptedException {
        userClient.deleteUser(userClient.getAccessToken());
    }
}
