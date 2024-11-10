package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.io.IOException;

public interface UsersClient {

    UserJson createUser(String username, String password);

    void sendInvitation(UserJson required, UserJson addressee);

    void addIncomeInvitation(UserJson targetUser, int count);

    void addOutcomeInvitation(UserJson targetUser, int count);

    void addFriend(UserJson required, UserJson addressee);

    void addFriend(UserJson targetUser, int count);
}
