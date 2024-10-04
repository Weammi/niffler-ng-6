package guru.qa.niffler.jupiter.extension;

import static guru.qa.niffler.data.tpl.Connections.closeAllConnections;

public class DatabasesExtension implements SuiteExtension {

    @Override
    public void afterSuite() {
        closeAllConnections();
    }
}