package guru.qa.niffler.test.web;

import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

class AuthTest {

    private UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    void jdbcWithRepository() { // Кэп: ролбэк не происходит
        UserJson user = usersDbClient.createUser(
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
        System.out.println(user);
    }

    @Test
    void jdbcWithoutTx() { // Кэп: ролбэк не происходит
        UserJson user = usersDbClient.createUserJdbcWithoutTx(
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
        System.out.println(user);
    }

    @Test
    void jdbcWithTx() { // Кэп: ролбэк не происходит
        UserJson user = usersDbClient.createUserJdbcWithTx(
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
        System.out.println(user);
    }

    @Test
    void springJdbcWithoutTx() { // Кэп: ролбэк не происходит
        UserJson user = usersDbClient.createUserSpringJdbcWithoutTx(
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
        System.out.println(user);
    }

    @Test
    void springJdbcWithTx() { // Кэп: ролбэк происходит
        UserJson user = usersDbClient.createUserSpringJdbcWithTx(
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
        System.out.println(user);
    }
}
