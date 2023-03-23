package requests;
import deserializator.order.get.Orders;
import deserializator.order.get.UserOrders;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import serialization.Order;
import specs.RequestSpec;
import urls.Endpoints;
import java.util.List;

public class OrderClient extends RequestSpec {
    private final Endpoints endpoints = new Endpoints();

    @Step("Создать заказ")
    public Response createOrder(Order order, String accessToken) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(accessToken)
                .when()
                .baseUri(endpoints.baseUri)
                .body(order)
                .when()
                .post(endpoints.orderUri);
    }

    @Step("Получить заказы пользователя")
    public UserOrders getOrdersOfUser(String accessToken) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(accessToken)
                .when()
                .baseUri(endpoints.baseUri)
                .when()
                .get(endpoints.orderUri)
                .body()
                .as(UserOrders.class);
    }

    @Step("Получить заказ пользователя")
    public Response getUserOrder(String accessToken) throws InterruptedException {
        return baseSpec()
                .auth().oauth2(accessToken)
                .when()
                .baseUri(endpoints.baseUri)
                .when()
                .get(endpoints.orderUri);
    }

    @Step("Проверить id пользовательских заказов")
    public int checkUserOrdersIds(UserOrders userOrders) {
        List<Orders> orderDataList = userOrders.getOrders();
        Orders ordersItem;
        int idCounter = 0;
        for (Orders orders : orderDataList) {
            ordersItem = orders;
            if (ordersItem.get_id() != null) {
                idCounter++;
            }
        }
        return idCounter;
    }
}

