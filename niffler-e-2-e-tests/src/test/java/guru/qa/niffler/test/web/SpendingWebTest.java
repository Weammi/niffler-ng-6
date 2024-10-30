package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@WebTest
class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            username = "weammi1",
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson[] spends, @UserType(EMPTY) StaticUser user) {
        SpendJson spend = spends[0];
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkAlert("New spending is successfully created");

        new MainPage()
                .getSearch().setSearch(newDescription)
                .checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void addSpend(UserJson user) {
        String category = randomCategoryName();
        String description = randomSentence(2);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickNewSpending()
                .setSpendingCategory(category)
                .setNewSpendingDescription(description)
                .setSpendingAmount("10")
                .getCalendar().selectDateInCalendar(new Date())
                .checkAlert("New spending is successfully created");

        new MainPage().checkThatTableContainsSpending(description);
    }
}

