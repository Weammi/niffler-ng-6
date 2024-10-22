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
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.utils.RandomDataUtils.*;

@WebTest
class SpendingWebTest {

    private static final Config CFG = Config.getInstance();
    private final UsersDbClient usersDbClient = new UsersDbClient();

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
                .save();

        new MainPage()
                .setSearch(newDescription)
                .checkThatTableContainsSpending(newDescription);
    }

    @Test
    void addSpend() {
        UserJson user = usersDbClient.createUser(randomUsername(), randomPassword());
        String category = randomCategoryName();
        String description = randomSentence(2);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .header.clickNewSpending()
                .setSpendingCategory(category)
                .setNewSpendingDescription(description)
                .setSpendingAmount("10")
                .calendar.selectDateInCalendar(new Date());
        new MainPage().checkThatTableContainsSpending(description);
    }
}

