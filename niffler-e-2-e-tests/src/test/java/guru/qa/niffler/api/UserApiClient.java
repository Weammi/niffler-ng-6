package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient.EmptyClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserApiClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final String defaultPassword = "12345";

    private final AuthApi authApi = new EmptyClient(CFG.authUrl()).create(AuthApi.class);
    private final UserdataApi userdataApi = new EmptyClient(CFG.userdataUrl()).create(UserdataApi.class);

    @Override
    @Step("Создать пользователя с именем {username} и паролем {password}")
    public @Nullable UserJson createUser(String username, String password) {
        try {
            authApi.requestRegister().execute();
            authApi.registerUser(
                    username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();

            UserJson userJson = requireNonNull(userdataApi.getCurrentUser(username).execute().body());
            return userJson.addTestData(
                    new TestData(
                            password
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при выполнении запроса на получение пользователя или ожидании", e);
        }
    }

    @Override
    @Step("Отправка приглашения от пользователя {user.username} пользователю {targetUser.username}")
    public void sendInvitation(@Nonnull UserJson user, @Nonnull UserJson targetUser) {
        final Response<UserJson> response;
        try {
            response = userdataApi.sendInvitation(user.username(), targetUser.username())
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Override
    @Step("Пользователю {targetUser.username} отправить {count} приглашений в друзья")
    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(username, defaultPassword);

                    response = userdataApi.sendInvitation(
                            newUser.username(),
                            targetUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .incomeInvitations()
                        .add(newUser);
            }
        }
    }

    @Override
    @Step("Пользователю {targetUser.username} отправить {count} приглашений в друзья")
    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                final UserJson newUser;
                try {
                    newUser = createUser(username, defaultPassword);

                    response = userdataApi.sendInvitation(
                            targetUser.username(),
                            newUser.username()
                    ).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .outcomeInvitations()
                        .add(newUser);
            }
        }
    }

    @Override
    @Step("Добавить в друзья пользователя {user.username} пользователю {targetUsername.username}")
    public void addFriend(UserJson user, UserJson targetUsername) {
        final Response<UserJson> response;
        try {
            userdataApi.sendInvitation(
                    user.username(),
                    targetUsername.username()
            ).execute();
            response = userdataApi.acceptInvitation(targetUsername.username(), user.username()).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());

        targetUsername.testData()
                .friends()
                .add(response.body());
    }

    @Override
    @Step("Добавить в друзья пользователю {targetUser.username} пользователей {count} штук")
    public void addFriend(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final String username = randomUsername();
                final Response<UserJson> response;
                try {
                    userdataApi.sendInvitation(
                            createUser(
                                    username,
                                    defaultPassword
                            ).username(),
                            targetUser.username()
                    ).execute();
                    response = userdataApi.acceptInvitation(targetUser.username(), username).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                assertEquals(200, response.code());

                targetUser.testData()
                        .friends()
                        .add(response.body());
            }
        }
    }
}
