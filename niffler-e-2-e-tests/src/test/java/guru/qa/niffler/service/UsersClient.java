package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.io.IOException;

public interface UsersClient {

    UserJson createUser(String username, String password) throws IOException, InterruptedException;

    void sendInvitation(UserJson required, UserJson addressee);

    void sendInvitation(UserJson targetUser, int count);

    void addFriend(UserJson required, UserJson addressee);

    void addFriend(UserJson targetUser, int count);
}
