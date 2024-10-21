package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

public class UserApiClient {

    private final UserApi userApi;

    public UserApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getInstance().userdataUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.userApi = retrofit.create(UserApi.class);
    }

    public UserJson getCurrentUser(String username) throws IOException {
        Response<UserJson> response = userApi.getCurrentUser(username).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Ошибка при получении пользователя - " + username);
        }
    }

    public UserJson updateUser(UserJson user) throws IOException {
        Response<UserJson> response = userApi.updateUser(user).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Ошибка при обновлении пользователя - " + user.username());
        }
    }

    public List<UserJson> getAllUsers(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getAllUsers(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Ошибка при получении пользователей");
        }
    }

    public List<UserJson> getFriends(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getFriends(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Ошибка при получении друзей для пользователя - " + username);
        }
    }

    public UserJson sendInvitation(String username, String targetUsername) throws IOException {
        Response<UserJson> response = userApi.sendInvitation(username, targetUsername).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException(format("Ошибка при добавлении в друзья пользователем %s пользователя %s ",
                    username, targetUsername));
        }
    }

    public UserJson declineInvitation(String username, String targetUsername) throws IOException {
        Response<UserJson> response = userApi.declineInvitation(username, targetUsername).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException(format("Ошибка при отклонении дружбы пользователем %s пользователя %s ",
                    username, targetUsername));
        }
    }

    public void removeFriend(String username, String targetUsername) throws IOException {
        Response<Void> response = userApi.removeFriend(username, targetUsername).execute();
        if (!response.isSuccessful()) {
            throw new IOException(format("Ошибка при удалении друга пользователем %s пользователя %s ",
                    username, targetUsername));
        }
    }
}
