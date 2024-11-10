package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
@Order(0)
class EmptyUserListTest {

    @Test
    @User
    void emptyUserList(UserJson user) {
        UserApiClient userApiClient = new UserApiClient();
        List<UserJson> response = userApiClient.getAllUsers(user, null);
        assertTrue(response.isEmpty(), "Список пользователей не пустой");
    }
}
