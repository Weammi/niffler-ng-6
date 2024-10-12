package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.util.Optional;
import java.util.UUID;


public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl());

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    spendRepository.create(SpendEntity.fromJson(spend));
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    return SpendJson.fromEntity(spendEntity);
                }
        );
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            SpendEntity updatedEntity = spendRepository.update(spendEntity);
            return SpendJson.fromEntity(updatedEntity);
        });
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepository.findById(id);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    @Override
    public Optional<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepository.findByUsernameAndSpendDescription(username, description);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(spendRepository.createCategory(categoryEntity));
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryById(id);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity =
                    spendRepository.findCategoryByUsernameAndSpendName(username, name);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    public void removeSpend(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendRepository.remove(spendEntity);
            return null;
        });
    }

    @Override
    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            spendRepository.removeCategory(categoryEntity);
            return null;
        });
    }
}
