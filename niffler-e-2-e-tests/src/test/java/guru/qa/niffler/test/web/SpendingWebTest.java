package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static com.codeborne.selenide.Selenide.sleep;

@WebTest
class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

//    @User(
//            spendings = @Spending(
//                    category = "Обучение",
//                    description = "Обучение Advanced 2.0",
//                    amount = 79990
//            )
//    )
//    @Test
//    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
//        final String newDescription = "Обучение Niffler Next Generation";
//
//        Selenide.open(CFG.frontUrl(), LoginPage.class)
//                .login(user.username(), user.testData().password())
//                .editSpending("Обучение Advanced 2.0")
//                .setNewSpendingDescription(newDescription)
//                .save()
//                .checkAlert("Spending is edited successfully");
//
//        new MainPage()
//                .getSearch().setSearch(newDescription)
//                .checkThatTableContainsSpending(newDescription);
//    }
//
//    @User
//    @Test
//    void addSpend(UserJson user) {
//        String category = randomCategoryName();
//        String description = randomSentence(2);
//
//        Selenide.open(CFG.frontUrl(), LoginPage.class)
//                .login(user.username(), user.testData().password())
//                .getHeader().clickNewSpending()
//                .setSpendingCategory(category)
//                .setNewSpendingDescription(description)
//                .setSpendingAmount("10")
//                .getCalendar().selectDateInCalendar(new Date())
//                .save()
//                .checkAlert("New spending is successfully created");
//
//        new MainPage().checkThatTableContainsSpending(description);
//    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expectedStatisticImage) throws IOException {
        MainPage mainPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatisticsIsDisplay();
        sleep(3000);
        mainPage
                .checkStatisticImage(expectedStatisticImage);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/clear-stat.png")
    void checkStatComponentAfterDeleteSpendTest(UserJson user, BufferedImage expectedStatisticImage) throws IOException {
        MainPage mainPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatisticsIsDisplay();

        mainPage
                .deleteSpending("Обучение Advanced 2.0");
        sleep(1000);
        mainPage
                .checkStatisticImage(expectedStatisticImage);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/edit-stat.png")
    void checkStatComponentAfterEditSpendTest(UserJson user, BufferedImage expectedStatisticImage) throws IOException {
        MainPage mainPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatisticsIsDisplay()
                .editSpending("Обучение Advanced 2.0")
                .setSpendingAmount("5000")
                .save()
                .checkAlert("New spending is successfully created")
                .checkStatisticCells(List.of("Обучение 5000"))
                .checkStatisticsIsDisplay();
        sleep(1000);
        mainPage
                .checkStatisticImage(expectedStatisticImage);
    }

    @User(
            categories = {
                    @Category(name = "Обучение"),
                    @Category(name = "Развлечения", archived = true),
                    @Category(name = "Продукты", archived = true)
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 1000
                    ),
                    @Spending(
                            category = "Развлечения",
                            description = "Поход в кино",
                            amount = 100
                    ),
                    @Spending(
                            category = "Продукты",
                            description = "Покупка продуктов",
                            amount = 3000
                    )
            }
    )
    @ScreenShotTest(value = "img/archived-stat.png")
    void checkStatComponentAfterArchivedCategoryTest(UserJson user, BufferedImage expectedStatisticImage) throws IOException {
        MainPage mainPage = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatisticsIsDisplay()
                .checkStatisticCells(List.of("Обучение 5000", "QA"))
                .checkStatisticsIsDisplay();
        sleep(1000);
        mainPage
                .checkStatisticImage(expectedStatisticImage);
    }
}

