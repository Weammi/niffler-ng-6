package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());

            ps.executeUpdate();

            final UUID generateKey;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generateKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            spend.setId(generateKey);
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                SpendEntity se = new SpendEntity();
                se.setId(rs.getObject("id", UUID.class));
                se.setUsername(rs.getString("username"));
                se.setSpendDate(rs.getDate("spend_date"));
                se.setCurrency(rs.getObject("currency", CurrencyValues.class));
                se.setAmount(rs.getDouble("amount"));
                se.setDescription(rs.getString("description"));
                se.setCategory(rs.getObject("category_id", CategoryEntity.class));

                if (rs.next()) {
                    return Optional.of(se);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendByUsernameAndDescription(String username, String description) {
        try (PreparedStatement statement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE username = ? AND description = ?"
        )) {
            statement.setString(1, username);
            statement.setString(2, description);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setCurrency(rs.getObject("currency", CurrencyValues.class));
                    se.setDescription(rs.getString("description"));
                    se.setCategory(rs.getObject("category_id", CategoryEntity.class));
                    return Optional.of(se);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        List<SpendEntity> spendList = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend"
        )) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SpendEntity se = new SpendEntity();
                    se.setId(rs.getObject("id", UUID.class));
                    se.setUsername(rs.getString("username"));
                    se.setSpendDate(rs.getDate("spend_date"));
                    se.setCurrency(rs.getObject("currency", CurrencyValues.class));
                    se.setAmount(rs.getDouble("amount"));
                    se.setDescription(rs.getString("description"));
                    se.setCategory(rs.getObject("category_id", CategoryEntity.class));

                    spendList.add(se);
                }
            }
            return spendList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"spend\" WHERE id = ?"
        )) {
            ps.setObject(1, spend.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllByCategoryId(UUID id) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "DELETE FROM spend WHERE category_id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
