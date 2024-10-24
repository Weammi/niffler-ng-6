package guru.qa.niffler.config;

import javax.annotation.Nonnull;

enum DockerConfig implements Config {
    INSTANCE;

    @Override
    @Nonnull
    public String frontUrl() {
        return "http://frontend.niffler.dc";
    }

    @Override
    @Nonnull
    public String spendUrl() {
        return "http://frontend.niffler.dc:8093/";
    }

    @Override
    @Nonnull
    public String spendJdbcUrl() {
        return "jdbc:postgresql://localhost:5432/niffler-spend";
    }

    @Override
    @Nonnull
    public String authUrl() {
        return "http://auth.niffler.dc:9000";
    }

    @Override
    @Nonnull
    public String authJdbcUrl() {
        return "jdbc:postgresql://localhost:5432/niffler-auth";
    }

    @Override
    @Nonnull
    public String gatewayUrl() {
        return "http://currency.niffler.dc:8091";
    }

    @Override
    @Nonnull
    public String userdataUrl() {
        return "http://userdata.niffler.dc:8089";
    }

    @Override
    @Nonnull
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://localhost:5432/niffler-userdata";
    }

    @Override
    @Nonnull
    public String currencyJdbcUrl() {
        return "jdbc:postgresql://localhost:5432/niffler-currency";
    }

    @Override
    @Nonnull
    public String ghUrl() {
        return "https://api.github.com/";
    }
}
