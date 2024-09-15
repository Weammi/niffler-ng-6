package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

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

    public RegisterPage setUsername(String username) {
        usernameInput.sendKeys(username);
        return new RegisterPage();
    }

    public RegisterPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return new RegisterPage();
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.sendKeys(password);
        return new RegisterPage();
    }

    public RegisterPage clickSignUpBtn() {
        signUpBtn.click();
        return new RegisterPage();
    }

    public RegisterPage checkCongratulationsText() {
        congratulationsText.shouldHave(text("Congratulations! You've registered!"));
        return new RegisterPage();
    }

    public RegisterPage checkSignInBtnIsDisplay() {
        signInBtn.shouldBe(visible);
        return new RegisterPage();
    }

    public RegisterPage checkErrorTextIsDisplay(String errorText) {
        this.errorText.shouldHave(text(errorText));
        return new RegisterPage();
    }
}