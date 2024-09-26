package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import java.sql.Connection;
import java.util.Optional;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    deleteAllSpendAndCategoryIfPresentByUsernameAndCategoryName(connection, spendEntity);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
        }, CFG.spendJdbcUrl());
    }

    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            new SpendDaoJdbc(connection).deleteSpend(spendEntity);
        }, CFG.spendJdbcUrl());
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
        }, CFG.spendJdbcUrl());
    }

    private void deleteAllSpendAndCategoryIfPresentByUsernameAndCategoryName(Connection connection, SpendEntity spendEntity) {
        Optional<CategoryEntity> categoryEntity = new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(
                spendEntity.getUsername(), spendEntity.getCategory().getName()
        );
        if (categoryEntity.isPresent()) {
            new SpendDaoJdbc(connection).deleteAllByCategoryId(categoryEntity.get().getId());
            new CategoryDaoJdbc(connection).deleteCategory(categoryEntity.get());
        }
    }
}
