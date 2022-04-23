import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserLoginTests {

    //Постоянный теестовый курьер для проверки логина
    private final String EMAIL = "login@mail.ru";
    private final String PASSWORD = "12345";
    //Объекты для тестов
    UserApiPattern userApi;
    UserDataPattern login = new UserDataPattern(EMAIL, PASSWORD);
    UserDataPattern loginWithIncorrectPassword = new UserDataPattern(EMAIL, "123456");
    UserDataPattern loginWithIncorrectEmail = new UserDataPattern("login@mail.ruu", PASSWORD);

    @Before
    public void setUp() {
        userApi = new UserApiPattern();
    }

    //Проверяем, что авторизация с корректными данными возвращает статус код = 200 и success = true
    @Test
    @DisplayName("Check correct login of user with valid credentials")
    public void checkCorrectLogin() {
        ValidatableResponse response = userApi.login(login);
        response.statusCode(200).and().assertThat().body("success", is(true));
    }

    //Проверяем, что авторизация с некорректным паролем возвращает статус код = 401 и success = false
    @Test
    @DisplayName("Check login of user with invalid password")
    public void checkLoginWithIncorrectPassword() {
        ValidatableResponse response = userApi.login(loginWithIncorrectPassword);
        response.statusCode(401).and().assertThat().body("success", is(false));
    }

    //Проверяем, что авторизация с некорректной почтой возвращает статус код = 401 и success = false
    @Test
    @DisplayName("Check login of user with invalid email")
    public void checkLoginWithIncorrectEmail() {
        ValidatableResponse response = userApi.login(loginWithIncorrectEmail);
        response.statusCode(401).and().assertThat().body("success", is(false));
    }
}
