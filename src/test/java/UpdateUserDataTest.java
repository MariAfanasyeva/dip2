import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import requests.UserClient;
import serialization.User;
import tools.UserDataGenerator;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserDataTest {
    private final UserDataGenerator uGen = new UserDataGenerator();
    private final User user = new User(uGen.genEmail(), uGen.genPassword(), uGen.genName());
    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Изменение email для авторизованного юзера")
    @Description("Проверяем код ответа и body")
    public void checkChangeAuthorizedUserEmail() throws InterruptedException {
        userClient.createUser(user);
        Response response = userClient.loginUser(user);
        userClient.setToken(response);
        User userWithChangingEmail = new User(uGen.genEmail(), null, null);
        userClient.changeUserData(userWithChangingEmail, userClient.getAccessToken()).then()
                .assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));

    }

    @Test
    @DisplayName("Изменение password для авторизованного юзера")
    @Description("Проверяем код ответа и body")
    public void checkChangeAuthorizedUserPassword() throws InterruptedException {
        userClient.createUser(user);
        Response response = userClient.loginUser(user);
        userClient.setToken(response);
        User userWithChangingPassword = new User(null, uGen.genPassword(), null);
        userClient.changeUserData(userWithChangingPassword, userClient.getAccessToken()).then()
                .assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение name для авторизованного юзера")
    @Description("Проверяем код ответа и body")
    public void checkChangeAuthorizedUserName() throws InterruptedException {
        userClient.createUser(user);
        Response response = userClient.loginUser(user);
        userClient.setToken(response);
        User userWithChangingName = new User(null, null, uGen.genName());
        userClient.changeUserData(userWithChangingName, userClient.getAccessToken()).then()
                .assertThat().statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение email для неавторизованного юзера")
    @Description("Проверяем код ответа и body")
    @Issue("Bug-001, 200 вместо 401")
    public void checkChangeUnAuthorizedUserEmail() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        User userWithChangingEmail = new User(uGen.genEmail(), null, null);
        userClient.changeUserData(userWithChangingEmail, userClient.getAccessToken()).then()
                .assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Изменение password для неавторизованного юзера")
    @Description("Проверяем код ответа и body")
    @Issue("Bug-002, 200 вместо 401")
    public void checkChangeUnAuthorizedUserPassword() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        User userWithChangingPassword = new User(null, uGen.genPassword(), null);
        userClient.changeUserData(userWithChangingPassword, userClient.getAccessToken()).then()
                .assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Изменение name для неавторизованного юзера")
    @Description("Проверяем код ответа и body")
    @Issue("Bug-003, 200 вместо 401")
    public void checkChangeUnAuthorizedUserName() throws InterruptedException {
        Response response = userClient.createUser(user);
        userClient.setToken(response);
        User userWithChangingName = new User(null, null, uGen.genName());
        userClient.changeUserData(userWithChangingName, userClient.getAccessToken()).then()
                .assertThat().statusCode(401)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Изменение email на уже существующий в системе")
    @Description("Проверяем код ответа и body")
    public void checkChangeEmailToExistingEmail() throws InterruptedException {
        userClient.createUser(user);//1 user
        User user2 = new User(uGen.genEmail(), uGen.genPassword(), uGen.genName());
        userClient.createUser(user2);
        Response response = userClient.loginUser(user2);//user2
        userClient.setToken(response);
        User user2ChangeEmail = new User(user.getEmail(), null, null);
        userClient.changeUserData(user2ChangeEmail, userClient.getAccessToken()).then()
                .assertThat().statusCode(403)
                .and()
                .body("message", equalTo("User with such email already exists"));
    }

    //удаляем юзера после тестирования
    @After
    public void clearUserData() throws InterruptedException {
        userClient.deleteUser(userClient.getAccessToken());
    }
}

