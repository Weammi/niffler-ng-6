package guru.qa.niffler.data.repository.impl.springJdbc;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    @Override
    public UserEntity create(UserEntity user) {
        userdataUserDao.create(user);
        return user;
    }

    @Override
    public UserEntity update(UserEntity user) {
        return userdataUserDao.update(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        userdataUserDao.addFriend(requester, addressee);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        userdataUserDao.sendInvitation(requester, addressee);
    }

    @Override
    public void remove(UserEntity user) {
        userdataUserDao.remove(user);
    }
}
