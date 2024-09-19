package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
class DisplayErrorIfPasswordAndConfirmPasswordNotEqualsTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void displayErrorIfPasswordAndConfirmPasswordNotEquals() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername("swan1")
                .setPassword("12345")
                .setPasswordSubmit("123456")
                .clickSignUpBtn()
                .checkErrorTextIsDisplay("Passwords should be equal");
    }
}
