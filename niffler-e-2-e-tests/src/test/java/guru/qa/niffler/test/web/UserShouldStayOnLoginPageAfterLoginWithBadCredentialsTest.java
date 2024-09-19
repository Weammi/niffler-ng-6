package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
class UserShouldStayOnLoginPageAfterLoginWithBadCredentialsTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .loginBadCredentials("badLogin", "badPassword")
                .checkErrorTextIsDisplay("Неверные учетные данные пользователя");
    }
}
