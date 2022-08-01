package ru.netology.test;

import com.codeborne.selenide.Condition;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;

public class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    @BeforeEach
    void setup() {
        //Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $x("//span[@class='button__text']").click();
        $(".heading").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(15));
    }

    @Test
    void shouldNotLitInWithInvalidLogin() {

        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue("invalid");
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $x("//span[@class='button__text']").click();
        $("[data-test-id=\"error-notification\"]").shouldHave(Condition.text("Ошибка"
                + " " + "Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    void shouldNotLitInWithInvalidPassword() {

        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue("invalid");
        $x("//span[@class='button__text']").click();
        $("[data-test-id=\"error-notification\"]").shouldHave(Condition.text("Ошибка"
                + " " + "Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    void shouldNotLitInactiveUser() {

        var registeredUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $x("//span[@class='button__text']").click();
        $(".notification .notification__content").shouldHave(Condition.text("Пользователь заблокирован"));

    }

}
