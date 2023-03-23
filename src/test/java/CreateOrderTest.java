import deserializator.order.create.OrderResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import requests.OrderClient;
import requests.UserClient;
import serialization.Order;
import serialization.User;
import tools.UserDataGenerator;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    private final UserDataGenerator uGen = new UserDataGenerator();
    private final User user = new User(uGen.genEmail(), uGen.genPassword(), uGen.genName());
    private final UserClient userClient = new UserClient();
    private final OrderResponse orderResponse = new OrderResponse();
    private final List<String> ingridientsForTest = new ArrayList<>();
    private final OrderClient orderClient = new OrderClient();

    //с десериализатором
    @Test
    @DisplayName("Создание заказ с авторизацией и валидными ингридиентами")
    @Description("У созданного заказа должен быть номер, код ответа 200")
    public void createOrderWithAuthorization() throws InterruptedException {
        ingridientsForTest.add("61c0c5a71d1f82001bdaaa6c");
        ingridientsForTest.add("61c0c5a71d1f82001bdaaa71");
        ingridientsForTest.add("61c0c5a71d1f82001bdaaa72");
        userClient.createUser(user);
        user.setName(null);
        Response response = userClient.loginUser(user);
        userClient.setToken(response);
        System.out.println(userClient.getAccessToken());
        Order order = new Order(ingridientsForTest);
        orderClient.createOrder(order, userClient.getAccessToken());
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказ без авторизации")
    @Description("Пользователь должен быть перенаправлен для авторизации: ошибка авторизации, код 401")
    @Issue("Bug-004, 200 вместо 401")
    public void createOrderWithoutAuthorization() throws InterruptedException {
        Response responseNewUser = userClient.createUser(user);
        userClient.setToken(responseNewUser);
        Order order = new Order(ingridientsForTest);
        Response response = orderClient.createOrder(order, userClient.getAccessToken());
        int statusCode = response.then().extract().statusCode();
        //Assert.assertEquals(401, 200); еще вариант проверки
        response.then().assertThat().statusCode(401);
    }

    @Test
    @DisplayName("Создание заказ с авторизацией, но без ингридиентов")
    @Description("Нельзя создать заказ без ингридиентов, код ответа 400")
    public void createOrderWithIngridients() throws InterruptedException {
        userClient.createUser(user);
        user.setName(null);
        Response response = userClient.loginUser(user);
        userClient.setToken(response);
        Order order = new Order(ingridientsForTest);
        Response response1 = orderClient.createOrder(order, userClient.getAccessToken());
        response1.then()
                .assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказ с авторизацией, но с неверным хэшем ингридиентов")
    @Description("Нельзя создать заказ без верного хэша ингридиентов, код ответа 500")
    public void createOrderWithWrongIngridientsHash() throws InterruptedException {
        ingridientsForTest.add("23233asssd344s444t3tgg4g");
        ingridientsForTest.add("87yhjbvjhfgipyu4yu7u4444");
        ingridientsForTest.add("o348yt34uhgirbgilerhuy38");
        userClient.createUser(user);
        user.setName(null);
        Response response = userClient.loginUser(user);
        userClient.setToken(response);
        Order order = new Order(ingridientsForTest);
        Response response1 = orderClient.createOrder(order, userClient.getAccessToken());
        response1.then().assertThat().statusCode(500);
    }

    //удаляем юзера после тестирования
    @After
    public void clearUserData() throws InterruptedException {
        userClient.deleteUser(userClient.getAccessToken());
    }
}

