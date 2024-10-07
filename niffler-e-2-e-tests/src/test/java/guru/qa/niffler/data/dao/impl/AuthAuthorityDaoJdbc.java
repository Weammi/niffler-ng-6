package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUser().getId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> authorityEntityList = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement("SELECT * FROM authority")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AuthorityEntity authority = new AuthorityEntity();
                authority.setId(rs.getObject("id", UUID.class));
                authority.setUser(rs.getObject("user_id", AuthUserEntity.class));
                authority.setAuthority(rs.getObject("authority", Authority.class));
                authorityEntityList.add(authority);
            }
            return authorityEntityList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
