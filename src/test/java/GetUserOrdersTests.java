import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class GetUserOrdersTests {

    //Данные пользователя с заказами для получения токена
    private final String EMAIL = "order@mail.ru";
    private final String PASSWORD = "12345";
    //Объекты для теста
    UserDataPattern loginForToken = new UserDataPattern(EMAIL, PASSWORD);
    OrderApiPattern orderApi;

    @Before
    public void setUp() {
        orderApi = new OrderApiPattern();
    }

    //Получаем заказы авторизованного пользователя
    @Test
    @DisplayName("Get user's orders with authorized user")
    public void getUserOrdersBeingAuthorized() {
        UserApiPattern userApi = new UserApiPattern();
        String token = userApi.login(loginForToken).extract().path("accessToken");
        ValidatableResponse response = orderApi.getUserOrdersWithAuth(token);
        response.statusCode(200).and().assertThat().body("success", is(true));
    }

    //Проверяем невозможность получить заказы будучи неавторизованным
    @Test
    @DisplayName("Get user's orders with unauthorized user")
    public void getUserOrdersWithoutAuthorization() {
        ValidatableResponse response = orderApi.getUserOrdersWithoutAuth();
        response.statusCode(401).and().assertThat().body("message", is("You should be authorised"));
    }
}
