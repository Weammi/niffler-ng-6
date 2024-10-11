package guru.qa.niffler.test.web;

import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

class AuthTest {

    private UsersDbClient usersDbClient = new UsersDbClient();

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

        usersDbClient.addInvitation(firstUser, secondUser);
        usersDbClient.addInvitation(secondUser, firstUser);
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

        usersDbClient.addInvitation(firstUser, secondUser);
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

        usersDbClient.addInvitation(user, 1);
    }
}
