package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomSentence;

@WebTest
class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending("Обучение Advanced 2.0")
                .setNewSpendingDescription(newDescription)
                .save()
                .checkAlert("Spending is edited successfully");

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
                .save()
                .checkAlert("New spending is successfully created");

        new MainPage().checkThatTableContainsSpending(description);
    }
}

