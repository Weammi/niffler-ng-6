package guru.qa.niffler.test.web;

import guru.qa.niffler.model.spend.CurrencyValues;
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
                new UserJson(
                        null,
                        "test 20",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson secondUser = usersDbClient.createUser(
                new UserJson(
                        null,
                        randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        usersDbClient.addFriend(firstUser, secondUser);
    }

    @Test
    void addInvitation() {
        UserJson firstUser = usersDbClient.createUser(
                new UserJson(
                        null,
                        randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        UserJson secondUser = usersDbClient.createUser(
                new UserJson(
                        null,
                        randomUsername(),
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );

        usersDbClient.addInvitation(firstUser, secondUser);
    }

    @ValueSource(strings = {
            "valentin-11",
            "valentin-12",
            "valentin-13"
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
