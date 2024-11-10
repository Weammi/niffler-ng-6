package guru.qa.niffler.test.web;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Disabled
class AuthTest {

    private UsersDbClient usersDbClient = new UsersDbClient();
    private SpendDbClient spendDbClient = new SpendDbClient();

    @Test
    void addFriend() {
        UserJson firstUser = usersDbClient.createUser(
                randomUsername(),
                "12345"
        );

        UserJson secondUser = usersDbClient.createUser(
                randomUsername(),
                "12345"
        );

        usersDbClient.sendInvitation(firstUser, secondUser);
        usersDbClient.sendInvitation(secondUser, firstUser);
        usersDbClient.addFriend(firstUser, secondUser);
    }

    @Test
    void addInvitation() {
        UserJson firstUser = usersDbClient.createUser(
                randomUsername(),
                "12345"
        );

        UserJson secondUser = usersDbClient.createUser(
                randomUsername(),
                "12345"
        );

        usersDbClient.sendInvitation(firstUser, secondUser);
    }

    @ValueSource(strings = {
            "test20",
            "test21",
            "test22"
    })
    @ParameterizedTest
    void springJdbcTest(String uname) {
        UserJson user = usersDbClient.createUser(
                uname,
                "12345"
        );

        usersDbClient.addIncomeInvitation(user, 1);
    }
}
