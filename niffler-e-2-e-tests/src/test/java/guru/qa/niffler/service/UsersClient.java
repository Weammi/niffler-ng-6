package guru.qa.niffler.service;

import guru.qa.niffler.model.userdata.UserJson;

public interface UsersClient {

    UserJson createUser(String username, String password);

    void sendInvitation(UserJson required, UserJson addressee);

    void sendInvitation(UserJson targetUser, int count);

    void addFriend(UserJson required, UserJson addressee);

    void addFriend(UserJson targetUser, int count);
}
