import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserLoginTests {

    //Постоянный теестовый пользователь для проверки логина
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

    //Логин существующего пользователя с корректными данными
    @Test
    @DisplayName("Correct login of the user with valid credentials")
    public void checkCorrectLogin() {
        ValidatableResponse response = userApi.login(login);
        response.statusCode(200).and().assertThat().body("success", is(true));
    }

    //Авторизация с некорректным паролем
    @Test
    @DisplayName("Login of the user with invalid password")
    public void checkLoginWithIncorrectPassword() {
        ValidatableResponse response = userApi.login(loginWithIncorrectPassword);
        response.statusCode(401).and().assertThat().body("message", is("email or password are incorrect"));
    }

    //Авторизация с некорректной почтой
    @Test
    @DisplayName("Login of the user with invalid email")
    public void checkLoginWithIncorrectEmail() {
        ValidatableResponse response = userApi.login(loginWithIncorrectEmail);
        response.statusCode(401).and().assertThat().body("message", is("email or password are incorrect"));
    }
}
