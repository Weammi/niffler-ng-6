package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
class RegisterNewUserTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void registerNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(randomUsername())
                .setPassword(randomPassword())
                .setPasswordSubmit(randomPassword())
                .clickSignUpBtn()
                .checkCongratulationsText()
                .checkSignInBtnIsDisplay();
    }
}
