package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {

    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findById(UUID id);

    List<SpendEntity> findSpendByUsernameAndDescription(String username, String description);

    List<SpendEntity> findAll();

    void remove(SpendEntity spend);

    void deleteAllByCategoryId(UUID id);
}
