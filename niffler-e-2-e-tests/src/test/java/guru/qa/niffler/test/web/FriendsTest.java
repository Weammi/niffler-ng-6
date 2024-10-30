package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
class FriendsTest {

    private static final Config CFG = Config.getInstance();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    void friendShouldBePresentInFriendTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .getHeader().clickAvatar()
                .clickFriends()
                .getSearch().setSearch("weammi3")
                .friendIsDisplayInFriendsList("weammi3");
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .getHeader().clickAvatar()
                .clickFriends()
                .friendTableIsEmpty();
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .getHeader().clickAvatar()
                .clickFriends()
                .getSearch().setSearch("weammi5")
                .friendIsDisplayInRequestsList("weammi5");
    }

    @Test
    void outcomeInvitationBePresentInAllPeopleTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .getHeader().clickAvatar()
                .clickAllPeople()
                .getSearch().setSearch("weammi5")
                .friendInvitationSent("weammi5");
    }

    @Test
    void acceptInvitation() {
        UserJson user = usersDbClient.createUser(randomUsername(), randomPassword());
        usersDbClient.sendInvitation(user, 1);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .acceptFriend()
                .shouldHaveMyFriendsListHeader()
                .checkUnfriendButtonIsVisible();
    }

    @Test
    void declineInvitation() {
        UserJson user = usersDbClient.createUser(randomUsername(), randomPassword());
        usersDbClient.sendInvitation(user, 1);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .declineFriend()
                .shouldHaveEmptyFriendsTable();
    }
}
