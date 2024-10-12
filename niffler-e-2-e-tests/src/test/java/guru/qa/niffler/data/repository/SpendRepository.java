package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

    SpendEntity create(SpendEntity spend);

    SpendEntity update(SpendEntity spend);

    Optional<SpendEntity> findById(UUID id);

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    void remove(SpendEntity spend);


    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

    void removeCategory(CategoryEntity category);
}
