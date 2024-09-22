package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;

@WebTest
class UserNotRegisterWithExistingUsernameTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void userNotRegisterWithExistingUsername(@UserType(EMPTY) StaticUser user) {
        final String errorText = "Username `" + user.username() + "` already exists";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createNewAccount()
                .setUsername(user.username())
                .setPassword(user.password())
                .setPasswordSubmit(user.password())
                .clickSignUpBtn()
                .checkErrorTextIsDisplay(errorText);
    }
}
