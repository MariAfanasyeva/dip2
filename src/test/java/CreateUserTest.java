import deserializator.user.UserResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import requests.UserClient;
import serialization.User;
import tools.UserDataGenerator;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    private final UserDataGenerator uGen = new UserDataGenerator();
    private final User user = new User(uGen.genEmail(), uGen.genPassword(), uGen.genName());
    private final UserClient userClient = new UserClient();


    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверяем код ответа и поле success")
    public void checkStatusCodeCreatingUser() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверяем поле email")
    public void checkUserEmail() throws InterruptedException {
        UserResponse userResponse = userClient.checkUserCreds(user);
        userClient.setAccessToken(userResponse);
        Assert.assertEquals(user.getEmail(), userResponse.getUser().getEmail());
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Проверяем поле name")
    public void checkUserName() throws InterruptedException {
        UserResponse userResponse = userClient.checkUserCreds(user);
        userClient.setAccessToken(userResponse);
        Assert.assertEquals(user.getName(), userResponse.getUser().getName());
    }

    //уникальность = email
    @Test
    @DisplayName("Создание ранее уже созданного пользователя")
    @Description("Проверяем, что повторное создание не произошло, вернулось корректное сообщение об ошибке и код ответа")
    public void createDouplicateUserReturnForbidden() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        User douplicateUser = new User(user.getEmail(), uGen.genPassword(), uGen.genName());
        Response response1 = userClient.createUser(douplicateUser);
        response1.then().assertThat().statusCode(403).and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля email")
    @Description("Проверяем, что создание не произошло, вернулось корректное сообщение об ошибке и код ответа ")
    public void createWithoutEmailReturnForbidden() throws InterruptedException {
        user.setEmail(null);
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля password")
    @Description("Проверяем, что создание не произошло, вернулось корректное сообщение об ошибке и код ответа")
    public void createWithoutPasswordReturnForbidden() throws InterruptedException {
        user.setPassword(null);
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля name")
    @Description("Проверяем, что создание не произошло, вернулось корректное сообщение об ошибке и код ответа")
    public void createWithoutNameReturnForbidden() throws InterruptedException {
        user.setName(null);
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    //удаляем юзера после тестирования
    @After
    public void clearUserData() throws InterruptedException {
        userClient.deleteUser(userClient.getAccessToken());
    }

}

