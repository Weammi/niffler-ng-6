package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();

    private final SpendDao spendDao = new SpendDaoJdbc();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {
        if (spend.getCategory().getId() == null) {
            spend.setCategory(findOrCreateCategoryForSpend(spend));
        }
        return spendDao.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        try (PreparedStatement spendPs = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "UPDATE public.spend " +
                        "SET spend_date = ?, currency = ?, amount = ?, description = ?, category_id = ?" +
                        "WHERE id = ?")
        ) {
            if (spend.getCategory().getId() == null) {
                spend.setCategory(findOrCreateCategoryForSpend(spend));
            }

            spendPs.setDate(1, new Date(spend.getSpendDate().getTime()));
            spendPs.setString(2, spend.getCurrency().name());
            spendPs.setDouble(3, spend.getAmount());
            spendPs.setString(4, spend.getDescription());
            spendPs.setObject(5, spend.getCategory().getId());
            spendPs.executeUpdate();

            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return spendDao.findById(id);
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        return spendDao.findSpendByUsernameAndDescription(username, description);
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDao.remove(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        return categoryDao.findByUsernameAndCategoryName(username, name);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.remove(category);
    }

    private CategoryEntity findOrCreateCategoryForSpend(SpendEntity spend) {
        Optional<CategoryEntity> existCategory = categoryDao
                .findByUsernameAndCategoryName(spend.getUsername(), spend.getCategory().getName());

        return existCategory.orElseGet(() -> categoryDao.create(spend.getCategory()));
    }
}
