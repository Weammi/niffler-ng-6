package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.utils.RandomDataUtils.*;

@WebTest
class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @User(
            username = "weammi1",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson[] categories, @UserType(EMPTY) StaticUser user) {
        CategoryJson category = categories[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .getHeader().clickAvatar()
                .clickProfile()
                .clickShowArchive()
                .checkArchiveCategoryIsDisplay(category.name());
    }

    @User(
            username = "weammi1",
            categories = @Category(
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson[] categories, @UserType(EMPTY) StaticUser user) {
        CategoryJson category = categories[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .getHeader().clickAvatar()
                .clickProfile()
                .checkActiveCategoryIsDisplay(category.name());
    }

    @Test
    void changeName() {
        UserJson user = usersDbClient.createUser(randomUsername(), randomPassword());
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