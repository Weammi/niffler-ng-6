package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

@WebTest
class FriendsTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @User(friends = 1)
    void friendShouldBePresentInFriendTable(UserJson user) {
        final String friendUsername = user.testData().friendsUsernames()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .getSearch().setSearch(friendUsername)
                .friendIsDisplayInFriendsList(friendUsername);
    }

    @Test
    @User()
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .friendTableIsEmpty();
    }

    @Test
    @User(incomeInvitations = 1)
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        final String friendUsername = user.testData().incomeInvitationsUsernames()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .getSearch().setSearch(friendUsername)
                .friendIsDisplayInRequestsList(friendUsername);
    }

    @Test
    @User(outcomeInvitations = 1)
    void outcomeInvitationBePresentInAllPeopleTable(UserJson user) {
        final String friendUsername = user.testData().outcomeInvitationsUsernames()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickAllPeople()
                .getSearch().setSearch(friendUsername)
                .friendInvitationSent(friendUsername);
    }

    @Test
    @User(incomeInvitations = 1)
    void acceptInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .acceptFriend()
                .shouldHaveMyFriendsListHeader()
                .checkUnfriendButtonIsVisible();
    }

    @User(friends = 1)
    @Test
    void shouldRemoveFriend(UserJson user) {
        final String userToRemove = user.testData().friendsUsernames()[0];

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .removeFriend(userToRemove)
                .friendTableIsEmpty();
    }

    @Test
    @User(incomeInvitations = 1)
    void declineInvitation(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .getHeader().clickAvatar()
                .clickFriends()
                .declineFriend()
                .shouldHaveEmptyFriendsTable();
    }
}
