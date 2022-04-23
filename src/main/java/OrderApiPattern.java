import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderApiPattern extends StellarBurgerRestPattern{

    private static final String GET_ORDERS_PATH = "/api/orders/";

    @Step("Get the user's orders with auth")
    public ValidatableResponse getUserOrdersWithAuth(String token) {

        return given().header("Authorization", token).spec(getBaseSpec()).get(GET_ORDERS_PATH).then();
    }

    @Step("Get the user's orders without")
    public ValidatableResponse getUserOrdersWithoutAuth() {

        return given().spec(getBaseSpec()).get(GET_ORDERS_PATH).then();
    }
}
