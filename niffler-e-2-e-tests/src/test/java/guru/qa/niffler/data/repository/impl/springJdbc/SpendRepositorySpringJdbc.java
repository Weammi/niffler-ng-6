package guru.qa.niffler.data.repository.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();

    private final SpendDao spendDao = new SpendDaoSpringJdbc();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {
        if (spend.getCategory().getId() == null) {
            spend.setCategory(findOrCreateCategory(spend));
        }

        return spendDao.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement spendPs = con.prepareStatement(
                    "UPDATE public.spend " +
                            "SET spend_date = ?, currency = ?, amount = ?, " +
                            "description = ?, category_id = ?" +
                            "WHERE id = ?"
            );

            if (spend.getCategory().getId() == null) {
                spend.setCategory(findOrCreateCategory(spend));
            }

            spendPs.setDate(1, new Date(spend.getSpendDate().getTime()));
            spendPs.setString(2, spend.getCurrency().name());
            spendPs.setDouble(3, spend.getAmount());
            spendPs.setString(4, spend.getDescription());
            spendPs.setObject(5, spend.getCategory().getId());
            return spendPs;
        });
        return spend;
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
        categoryDao.create(category);
        return category;
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

    private CategoryEntity findOrCreateCategory(SpendEntity spend) {
        Optional<CategoryEntity> existCategory = categoryDao
                .findByUsernameAndCategoryName(spend.getUsername(), spend.getCategory().getName());

        return existCategory.orElseGet(() -> categoryDao.create(spend.getCategory()));
    }
}
