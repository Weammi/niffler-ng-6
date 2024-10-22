package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement createNewAccountBtn = $("[class='form__register']");
    private final SelenideElement errorText = $("[class*='form__error']");

    @Step("Авторизоваться пользователем с логином {username} и паролем {password}")
    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    @Step("Авторизоваться пользователем с не существующим логином {username} и паролем {password}")
    public LoginPage loginBadCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return this;
    }

    @Step("Нажать кнопку \"Create new account\"")
    public RegisterPage createNewAccount() {
        createNewAccountBtn.click();
        return new RegisterPage();
    }

    @Step("Отображается ошибка с текстом - {errorText}")
    public LoginPage checkErrorTextIsDisplay(String errorText) {
        this.errorText.shouldHave(text(errorText));
        return this;
    }
}
