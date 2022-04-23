import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserApiPattern extends StellarBurgerRestPattern {

    private static final String REGISTRATION_PATH = "api/auth/register/";
    private static final String LOGIN_PATH = "api/auth/login/";
    private static final String CHANGE_USER_DATA_PATH = "api/auth/user/";

    @Step("Registration of user")
    public ValidatableResponse registration(UserDataPattern user) {

        return given().spec(getBaseSpec()).body(user).post(REGISTRATION_PATH).then();
    }

    @Step("Login of user")
    public ValidatableResponse login(UserDataPattern user) {

        return given().spec(getBaseSpec()).body(user).post(LOGIN_PATH).then();
    }

    @Step("Change user data with authorization")
    public ValidatableResponse changeDataWithAuth(UserDataPattern loginData, UserDataPattern updateData) {

        String bearerToken = given().spec(getBaseSpec()).body(loginData).post(LOGIN_PATH).then().extract().path("accessToken");

        return given().header("Authorization", bearerToken)
                .spec(getBaseSpec()).body(updateData).patch(CHANGE_USER_DATA_PATH).then();
    }

    @Step("Change user data without authorization")
    public ValidatableResponse changeDataWithoutAuth(UserDataPattern updateData) {

        return given().spec(getBaseSpec()).body(updateData).patch(CHANGE_USER_DATA_PATH).then();
    }

}
