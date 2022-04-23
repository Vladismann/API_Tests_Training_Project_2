import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UserRegistrationTests {

    //Генерация имени и почты для тестового пользователя, пароль одинаковый для удобства
    private final String NAME = "test" + RandomStringUtils.randomAlphabetic(3);
    private final String EMAIL = "test" + RandomStringUtils.randomAlphabetic(3) + "@mail.ru";
    private final String PASSWORD = "12345";
    //Объекты для тестов
    UserApiPattern userApi;
    UserDataPattern registration = new UserDataPattern(NAME, EMAIL, PASSWORD);
    UserDataPattern registrationWithoutName = new UserDataPattern("", EMAIL, PASSWORD);
    UserDataPattern registrationWithoutEmail = new UserDataPattern(NAME, "", PASSWORD);
    UserDataPattern registrationWithoutPassword = new UserDataPattern(NAME, EMAIL, "");

    @Before
    public void setUp() {
        userApi = new UserApiPattern();
    }

    //Регистрация уникального пользователя с корректными данными
    @Test
    @DisplayName("Correct registration of the user with valid credentials")
    public void checkRegistration() {
        ValidatableResponse response = userApi.registration(registration);
        response.statusCode(200).and().assertThat().body("success", is(true));
    }

    //Регистрация существующего пользователя
    @Test
    @DisplayName("Registration of the same user")
    public void checkRegistrationOfTheSameUser() {
        userApi.registration(registration);
        ValidatableResponse response = userApi.registration(registration);
        response.statusCode(403).and().assertThat().body("message", is("User already exists"));
    }

    //Регистрация пользователя без имени
    @Test
    @DisplayName("Registration of the user without name")
    public void checkRegistrationOfUserWithoutName() {
        ValidatableResponse response = userApi.registration(registrationWithoutName);
        response.statusCode(403).and().assertThat().body("message", is("Email, password and name are required fields"));
    }

    //Регистрация пользователя без почты
    @Test
    @DisplayName("Registration of the user without email")
    public void checkRegistrationOfUserWithoutEmail() {
        ValidatableResponse response = userApi.registration(registrationWithoutEmail);
        response.statusCode(403).and().assertThat().body("message", is("Email, password and name are required fields"));
    }

    //Регистрация пользователя без пароля
    @Test
    @DisplayName("Registration the of user without password")
    public void checkRegistrationOfUserWithoutPassword() {
        ValidatableResponse response = userApi.registration(registrationWithoutPassword);
        response.statusCode(403).and().assertThat().body("message", is("Email, password and name are required fields"));
    }
}
