package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.userdata.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.data.Databases.xaTransaction;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(
                xaTransaction(
                        new Databases.XaFunction<>(
                                con -> {
                                    AuthUserEntity authUser = new AuthUserEntity();
                                    authUser.setUsername(user.username());
                                    authUser.setPassword(pe.encode(randomPassword()));
                                    authUser.setEnabled(true);
                                    authUser.setAccountNonExpired(true);
                                    authUser.setAccountNonLocked(true);
                                    authUser.setCredentialsNonExpired(true);
                                    new AuthUserDaoJdbc(con).create(authUser);
                                    new AuthAuthorityDaoJdbc(con).create(
                                            Arrays.stream(Authority.values())
                                                    .map(a -> {
                                                                AuthorityEntity ae = new AuthorityEntity();
                                                                ae.setUserId(authUser.getId());
                                                                ae.setAuthority(a);
                                                                return ae;
                                                            }
                                                    ).toArray(AuthorityEntity[]::new));
                                    return null;
                                },
                                CFG.authJdbcUrl()
                        ),
                        new Databases.XaFunction<>(
                                con -> {
                                    UserEntity ue = new UserEntity();
                                    ue.setUsername(user.username());
                                    ue.setFullname(user.fullname());
                                    ue.setCurrency(user.currency());
                                    new UserDaoJdbc(con).createUser(ue);
                                    return ue;
                                },
                                CFG.userdataJdbcUrl()
                        )
                ),
                null);
    }
}