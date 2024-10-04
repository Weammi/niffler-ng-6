package guru.qa.niffler.data.tpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class Connections {

    private static final Map<String, JdbcConnectionHolder> holders = new ConcurrentHashMap<>();

    private Connections() {
    }

    public static JdbcConnectionHolder holder(String jdbcUrl) {
        return holders.computeIfAbsent(
                jdbcUrl,
                key -> new JdbcConnectionHolder(
                        dataSource(jdbcUrl)
                )
        );
    }

    public static JdbcConnectionHolders holders(String... jdbcUrl) {
        List<JdbcConnectionHolder> result = new ArrayList<>();
        for (String url : jdbcUrl) {
            result.add(holder(url));
        }
        return new JdbcConnectionHolders(result);
    }

    public static void closeAllConnections() {
        holders.values().forEach(JdbcConnectionHolder::closeAllConnections);
    }
}
