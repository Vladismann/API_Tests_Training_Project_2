import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UpdateUserDataTests {

    //Постоянный тестовый пользователь для изменения данных пользователя
    private final String NAME = "update";
    private final String EMAIL = "update@mail.ru";
    private final String PASSWORD = "12345";
    //Почта существующего пользователя
    private final String EXISTING_EMAIL = "test@mail.ru";
    //Новые данные для изменения
    private final String NEW_NAME = "newData";
    private final String NEW_EMAIL = "newData@mail.ru";
    private final String NEW_PASSWORD = "123456";
    //Объекты для тестов
    UserApiPattern userApi;
    UserDataPattern userData = new UserDataPattern(NAME, EMAIL, PASSWORD);
    UserDataPattern loginForTokenWithFirstUserData = new UserDataPattern(EMAIL, PASSWORD);
    UserDataPattern newUserData = new UserDataPattern(NEW_NAME, NEW_EMAIL, NEW_PASSWORD);
    UserDataPattern loginForTokenWithNewUserData = new UserDataPattern(NEW_EMAIL, NEW_PASSWORD);
    UserDataPattern dataWithExistingEmail = new UserDataPattern(NEW_NAME, EXISTING_EMAIL, NEW_PASSWORD);

    @Before
    public void setUp() {
        userApi = new UserApiPattern();
    }

    //Меняем данные тестового пользователя на новые с авторизацией и после теста возвращаем старые данные
    @Test
    @DisplayName("Check updating of all user data with authorized user")
    public void checkUpdatingOfUserDataBeingAuthorized() {
        ValidatableResponse response = userApi.changeDataWithAuth(loginForTokenWithFirstUserData, newUserData);
        response.statusCode(200).and().assertThat().body("success", is(true));
        //Возвращаем изначальные данные
        userApi.changeDataWithAuth(loginForTokenWithNewUserData, userData);
    }

    //Меняем данные тестового пользователя на новые с уже использующейся почтой
    @Test
    @DisplayName("Check updating of existing email data with authorized user")
    public void checkUpdatingOfUserDataForExistingEmail() {
        ValidatableResponse response = userApi.changeDataWithAuth(loginForTokenWithFirstUserData, dataWithExistingEmail);
        response.statusCode(403).and().assertThat().body("message", is("User with such email already exists"));
    }

    //Меняем данные пользователя без авторизации
    @Test
    @DisplayName("Check updating of all user data without authorization")
    public void checkUpdatingOfUserDataWithoutAuthorization() {
        ValidatableResponse response = userApi.changeDataWithoutAuth(newUserData);
        response.statusCode(401).and().assertThat().body("message", is("You should be authorised"));
    }
}
