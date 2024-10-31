package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.ENTER;

public class Search<T extends BasePage<?>> extends BaseComponent<T> {

    private final SelenideElement searchInput = $("input[type='text']");

    public Search(SelenideElement self, T page) {
        super(self, page);
    }

    @Step("Ввести в поле поиска - {spendingName}")
    public T setSearch(String spendingName) {
        searchInput.sendKeys(spendingName);
        searchInput.sendKeys(ENTER);

        return getPage();
    }
}