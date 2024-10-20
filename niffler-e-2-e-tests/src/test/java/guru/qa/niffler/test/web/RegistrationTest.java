package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
class RegistrationTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void userNotRegisterWithExistingUsername(@UsersQueueExtension.UserType(EMPTY) UsersQueueExtension.StaticUser user) {
        final String errorText = "Username `" + user.username() + "` already exists";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(user.username())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .clickSignUpBtn()
                .checkErrorTextIsDisplay(errorText);
    }

    @Test
    void registerNewUser() {
        final String pass = randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(randomUsername())
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSignUpBtn()
                .checkCongratulationsText()
                .checkSignInBtnIsDisplay();
    }

    @Test
    void displayErrorIfPasswordAndConfirmPasswordNotEquals() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(randomUsername())
                .setPassword(randomPassword())
                .setPasswordSubmit("123456")
                .clickSignUpBtn()
                .checkErrorTextIsDisplay("Passwords should be equal");
    }
}
