package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.ENTER;

public class SearchField {

    private final SelenideElement searchInput = $("input[type='text']");

    @Step("Ввести в поле поиска - {spendingName}")
    public void setSearch(String spendingName) {
        searchInput.sendKeys(spendingName);
        searchInput.sendKeys(ENTER);
    }
}