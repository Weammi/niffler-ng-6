package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomName;

@WebTest
class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
        final String categoryName = user.testData().categoryDescriptions()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickProfile()
                .clickShowArchive()
                .checkArchiveCategoryIsDisplay(categoryName);
    }

    @User(
            categories = @Category(
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserJson user) {
        final String categoryName = user.testData().categoryDescriptions()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickProfile()
                .checkActiveCategoryIsDisplay(categoryName);
    }

    @Test
    @User
    void changeName(UserJson user) {
        String name = randomName();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickProfile()
                .setName(name)
                .clickSaveBtn()
                .shouldBeVisibleSaveChangesSuccessMessage()
                .checkName(name);
    }
}