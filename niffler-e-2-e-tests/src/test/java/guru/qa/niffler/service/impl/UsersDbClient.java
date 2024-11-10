package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private static final String defaultPassword = "12345";

    private final AuthUserRepository authUserRepositoryHibernate = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepositoryHibernate = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl());

    @Override
    @Step("Создать пользователя с логином {username} и паролем {password}")
    public UserJson createUser(String username, String password) {
        return requireNonNull(
                xaTransactionTemplate.execute(
                        () -> UserJson.fromEntity(
                                createNewUser(username, password),
                                null
                        )
                )
        );
    }

    @Override
    @Step("Добавить в друзья пользователем {required.username} пользователя {UserJson.username}")
    public void addFriend(UserJson required, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
            userdataUserRepositoryHibernate.addFriend(
                    UserEntity.fromJson(required),
                    UserEntity.fromJson(addressee)
            );
            return null;
        });
    }

    @Override
    @Step("Пользователю {targetUser.username} добавить друзей в кол-ве {count}")
    public void addFriend(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                targetUser.testData()
                        .friends()
                        .add(UserJson.fromEntity(
                                        requireNonNull(
                                                xaTransactionTemplate.execute(() -> {
                                                            final String username = randomUsername();
                                                            final UserEntity newUser = createNewUser(username, defaultPassword);
                                                            userdataUserRepositoryHibernate.addFriend(
                                                                    targetEntity,
                                                                    newUser
                                                            );
                                                            return newUser;
                                                        }
                                                )
                                        ),
                                        FriendState.FRIEND
                                )
                        );
            }
        }
    }

    @Step("Добавление входящих приглашений в друзья" +
            "от пользователя {addressee.username} пользователю: {required.username}")
    public void sendInvitation(UserJson required, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
            userdataUserRepositoryHibernate.sendInvitation(
                    UserEntity.fromJson(required),
                    UserEntity.fromJson(addressee)
            );
            return null;
        });
    }

    @Override
    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                targetUser.testData()
                        .incomeInvitations()
                        .add(UserJson.fromEntity(
                                        requireNonNull(
                                                xaTransactionTemplate.execute(() -> {
                                                            final String username = randomUsername();
                                                            final UserEntity newUser = createNewUser(username, defaultPassword);
                                                            userdataUserRepositoryHibernate.sendInvitation(
                                                                    newUser,
                                                                    targetEntity
                                                            );
                                                            return newUser;
                                                        }
                                                )
                                        ),
                                        FriendState.INVITE_RECEIVED
                                )
                        );
            }
        }
    }

    @Override
    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                targetUser.testData()
                        .outcomeInvitations()
                        .add(UserJson.fromEntity(
                                        requireNonNull(
                                                xaTransactionTemplate.execute(() -> {
                                                            final String username = randomUsername();
                                                            final UserEntity newUser = createNewUser(username, defaultPassword);
                                                            userdataUserRepositoryHibernate.sendInvitation(
                                                                    targetEntity,
                                                                    newUser
                                                            );
                                                            return newUser;
                                                        }
                                                )
                                        ),
                                        FriendState.INVITE_RECEIVED
                                )
                        );
            }
        }
    }

    @Nonnull
    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    @Nonnull
    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

    @Nonnull
    private UserEntity createNewUser(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepositoryHibernate.create(authUser);
        return userdataUserRepositoryHibernate.create(userEntity(username));
    }
}
