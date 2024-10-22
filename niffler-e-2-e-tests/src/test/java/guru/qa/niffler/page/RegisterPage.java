package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpBtn = $("button[type='submit']");
    private final SelenideElement congratulationsText = $("[class*='form__paragraph']");
    private final SelenideElement signInBtn = $("[class*='form_sign-in']");
    private final SelenideElement errorText = $("[class*='form__error']");

    @Step("Ввести имя - {username}")
    public RegisterPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    @Step("Ввести пароль - {password}")
    public RegisterPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    @Step("Ввести повторный пароль - {password}")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.sendKeys(password);
        return this;
    }

    @Step("Нажать кнопку зарегистрироваться")
    public RegisterPage clickSignUpBtn() {
        signUpBtn.click();
        return this;
    }

    @Step("Текст успешной регистрации отображается")
    public RegisterPage checkCongratulationsText() {
        congratulationsText.shouldHave(text("Congratulations! You've registered!"));
        return this;
    }

    @Step("Отображается кнопка \"Вход\"")
    public RegisterPage checkSignInBtnIsDisplay() {
        signInBtn.shouldBe(visible);
        return this;
    }

    @Step("Отображается ошибка с текстом - {errorText}")
    public RegisterPage checkErrorTextIsDisplay(String errorText) {
        this.errorText.shouldHave(text(errorText));
        return this;
    }
}
