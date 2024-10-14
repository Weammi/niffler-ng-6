package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.userdataJdbcUrl());

    @Override
    public UserEntity create(UserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public UserEntity update(UserEntity user) {
        UserEntity toBeUpdated = findById(user.getId()).get();
        entityManager.joinTransaction();
        toBeUpdated.setCurrency(user.getCurrency());
        toBeUpdated.setFirstname(user.getFirstname());
        toBeUpdated.setSurname(user.getSurname());
        toBeUpdated.setPhoto(user.getPhoto());
        toBeUpdated.setPhotoSmall(user.getPhotoSmall());
        toBeUpdated.setFullname(user.getFullname());
        return entityManager.merge(toBeUpdated);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(UserEntity.class, id)
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("select u from UserEntity u where u.username =: username", UserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();
        Date date = new Date();
        FriendshipEntity requesterFriendship = new FriendshipEntity();
        requesterFriendship.setRequester(requester);
        requesterFriendship.setAddressee(addressee);
        requesterFriendship.setStatus(ACCEPTED);
        requesterFriendship.setCreatedDate(date);

        FriendshipEntity addresseeFriendship = new FriendshipEntity();
        addresseeFriendship.setRequester(addressee);
        addresseeFriendship.setAddressee(requester);
        addresseeFriendship.setStatus(ACCEPTED);
        addresseeFriendship.setCreatedDate(date);

        entityManager.persist(requesterFriendship);
        entityManager.persist(addresseeFriendship);

        requester.addFriends(ACCEPTED, addressee);
        addressee.addFriends(ACCEPTED, requester);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();
        FriendshipEntity friendship = new FriendshipEntity();
        friendship.setRequester(requester);
        friendship.setAddressee(addressee);
        friendship.setStatus(PENDING);
        friendship.setCreatedDate(new Date());
        entityManager.persist(friendship);
        requester.addFriends(PENDING, addressee);
    }

    @Override
    public void remove(UserEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(user);
    }
}
