package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
class UserNotRegisterWithExistingUsernameTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void userNotRegisterWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername("duck")
                .setPassword("12345")
                .setPasswordSubmit("12345")
                .clickSignUpBtn()
                .checkErrorTextIsDisplay("Username `duck` already exists");
    }
}
