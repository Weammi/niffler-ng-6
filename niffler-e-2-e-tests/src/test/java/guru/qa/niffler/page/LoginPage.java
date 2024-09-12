package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement createNewAccountBtn = $("[class='form__register']");
    private final SelenideElement errorText = $("[class*='form__error']");

    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    public LoginPage loginBadCredentials(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new LoginPage();
    }

    public RegisterPage createNewAccount() {
        createNewAccountBtn.click();
        return new RegisterPage();
    }

    public void checkErrorTextIsDisplay(String errorText) {
        this.errorText.shouldHave(text(errorText));
    }
}
