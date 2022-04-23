import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class CreateOrdersTests {

    //Данные пользователя для получения токена
    private final String EMAIL = "order@mail.ru";
    private final String PASSWORD = "12345";
    //Ингредиенты для создания заказа
    String[] ingredient = {"61c0c5a71d1f82001bdaaa6d"};
    String[] emptyIngredient = {};
    String[] invalidIngredient = {"61c0c5a71d1f82001bdAAA6"};
    //Объекты для теста
    UserDataPattern loginForToken = new UserDataPattern(EMAIL, PASSWORD);
    CreationOfOrderDataPattern order = new CreationOfOrderDataPattern(ingredient);
    CreationOfOrderDataPattern emptyOrder = new CreationOfOrderDataPattern(emptyIngredient);
    CreationOfOrderDataPattern invalidOrder = new CreationOfOrderDataPattern(invalidIngredient);
    OrderApiPattern orderApi;

    @Before
    public void setUp() {
        orderApi = new OrderApiPattern();
    }

    //Создаем заказ авторизованным пользователем
    @Test
    @DisplayName("Create order with authorized user")
    public void checkCreationOfOrderWithAuth() {
        UserApiPattern userApi = new UserApiPattern();
        String token = userApi.login(loginForToken).extract().path("accessToken");
        ValidatableResponse response = orderApi.createOrderWithAuth(order, token);
        response.statusCode(200).and().assertThat().body("success", is(true));
    }

    //Создаем заказ без ингредиента
    @Test
    @DisplayName("Create order with authorized user without ingredients")
    public void checkCreationOfOrderWithAuthWithoutIngredients() {
        UserApiPattern userApi = new UserApiPattern();
        String token = userApi.login(loginForToken).extract().path("accessToken");
        ValidatableResponse response = orderApi.createOrderWithAuth(emptyOrder, token);
        response.statusCode(400).and().assertThat().body("message", is("Ingredient ids must be provided"));
    }

    //Создаем заказ с некорректным ингредиентом
    @Test
    @DisplayName("Create order with authorized user with invalid ingredient")
    public void checkCreationOfOrderWithAuthWithInvalidIngredient() {
        UserApiPattern userApi = new UserApiPattern();
        String token = userApi.login(loginForToken).extract().path("accessToken");
        ValidatableResponse response = orderApi.createOrderWithAuth(invalidOrder, token);
        response.statusCode(500);
    }

    //Создаем заказ без авторизации
    @Test
    @DisplayName("Create order with unauthorized user")
    public void checkCreationOfOrderWithoutAuthorization() {
        ValidatableResponse response = orderApi.createOrderWithoutAuth(order);
        response.statusCode(200).assertThat().body("success", is(true));
    }
}
