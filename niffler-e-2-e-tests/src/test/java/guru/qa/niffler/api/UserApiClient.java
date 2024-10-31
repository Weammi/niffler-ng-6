package guru.qa.niffler.api;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserApiClient extends RestClient implements UsersClient {

    private final UserApi userApi;
    private final AuthApiClient authApiClient = new AuthApiClient();

    public UserApiClient() {
        super(CFG.userdataUrl());
        this.userApi = retrofit.create(UserApi.class);
    }

    @Override
    @Step("Создать пользователя с именем {username} и паролем {password}")
    public @Nullable UserJson createUser(String username, String password) {
        Stopwatch sw = Stopwatch.createStarted();

        authApiClient.requestRegisterForm();
        authApiClient.registerUser(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        );

        while (sw.elapsed(TimeUnit.MILLISECONDS) < 10_000L) {
            try {
                UserJson userJson = userApi.getCurrentUser(username).execute().body();
                if (userJson != null && userJson.id() != null) {
                    return userJson;
                } else {
                    Thread.sleep(100);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Ошибка при выполнении запроса на получение пользователя или ожидании", e);
            }
        }
        throw new AssertionError("Пользователь не был найден в системе после истечения времени ожидания");
    }

    @Override
    @Step("Отправка приглашения от пользователя {user.username} пользователю {targetUser.username}")
    public void sendInvitation(@Nonnull UserJson user, @Nonnull UserJson targetUser) {
        final Response<UserJson> response;
        try {
            response = userApi.sendInvitation(user.username(), targetUser.username())
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Override
    @Step("Пользователю {targetUser.username} отправить {count} приглашений в друзья")
    public void sendInvitation(UserJson targetUser, int count) {
        if (count != 0) {
            UserJson newUser;
            for (int i = 0; i < count; i++) {
                newUser = createUser(randomUsername(), "12345");
                sendInvitation(newUser, targetUser);
            }
        }
    }

    @Step("Принятие приглашения от пользователя {user.username} пользователем {targetUser.username}")
    public void acceptInvitation(@Nonnull UserJson user, @Nonnull UserJson targetUser) {
        final Response<UserJson> response;
        try {
            response = userApi.acceptInvitation(user.username(), targetUser.username())
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Override
    @Step("Добавить в друзья пользователя {user.username} пользователю {targetUsername.username}")
    public void addFriend(UserJson user, UserJson targetUsername) {
        sendInvitation(user, targetUsername);
        acceptInvitation(user, targetUsername);
    }

    @Override
    public void addFriend(UserJson targetUser, int count) {
        if (count != 0) {
            UserJson newUser;
            for (int i = 0; i < count; i++) {
                newUser = createUser(randomUsername(), "12345");
                addFriend(newUser, targetUser);
            }
        }
    }
}
