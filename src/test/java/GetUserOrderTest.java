import deserializator.order.get.UserOrders;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import requests.OrderClient;
import requests.UserClient;
import serialization.Order;
import serialization.User;
import tools.UserDataGenerator;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetUserOrderTest {
    private final UserDataGenerator uGen = new UserDataGenerator();
    private final User user = new User(uGen.genEmail(), uGen.genPassword(), uGen.genName());
    private final UserClient userClient = new UserClient();
    private final UserOrders userOrders = new UserOrders();
    private final List<String> ingridientsForTest = new ArrayList<>();
    private final OrderClient orderClient = new OrderClient();

    @Before
    public void ingridientList() {
        ingridientsForTest.add("61c0c5a71d1f82001bdaaa6c");
        ingridientsForTest.add("61c0c5a71d1f82001bdaaa71");
        ingridientsForTest.add("61c0c5a71d1f82001bdaaa72");
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверяем, что возвращается список заказов с id, код ответа 200")
    public void getAuthorizedUsersOrders() throws InterruptedException {
        userClient.createUser(user); //создаем клиента
        Response response1 = userClient.loginUser(user); //логиним клиента
        userClient.setToken(response1);//устанавливаем токен
        Order order = new Order(ingridientsForTest);//ингридиенты
        orderClient.createOrder(order, userClient.getAccessToken());//создать заказ
        UserOrders response = orderClient.getOrdersOfUser(userClient.getAccessToken());//получить заказ
        int statusCode = orderClient.getUserOrder(userClient.getAccessToken()).statusCode();
        Assert.assertTrue(statusCode == 200
                && orderClient.checkUserOrdersIds(response) > 0);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Проверяем тело и код ответа 401")
    public void getUnauthorizedUsersOrders() throws InterruptedException {
        String accessToken = "";
        Response response = orderClient.getUserOrder(accessToken);
        response.then().assertThat().body("message", equalTo("You should be authorised"))
                .and().statusCode(401);
    }

    //удаляем юзера после тестирования
    @After
    public void clearUserData() throws InterruptedException {
        userClient.deleteUser(userClient.getAccessToken());
    }
}
