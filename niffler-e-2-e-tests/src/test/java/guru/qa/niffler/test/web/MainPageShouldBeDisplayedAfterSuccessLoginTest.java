package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
class MainPageShouldBeDisplayedAfterSuccessLoginTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345")
                .checkHistoryOfSpendingsIsDisplay()
                .checkStatisticsIsDisplay();
    }
}