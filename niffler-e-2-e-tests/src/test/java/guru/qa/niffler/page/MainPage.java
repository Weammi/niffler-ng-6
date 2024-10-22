package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement spendings = $("#spendings");
    private final SelenideElement statistics = $("#stat");

    public Header header = new Header();
    public SearchField searchField = new SearchField();

    @Step("Нажать на кнопку редактирования траты")
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("В списке отображается трата - {spendingDescription}")
    public MainPage checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
        return this;
    }

    @Step("Отображается история расходов")
    public MainPage checkHistoryOfSpendingsIsDisplay() {
        spendings.shouldBe(visible);
        return this;
    }

    @Step("Отображается статистика")
    public MainPage checkStatisticsIsDisplay() {
        statistics.shouldBe(visible);
        return this;
    }

    @Step("Ввести в поле поиска - {name}")
    public MainPage setSearch(String name) {
        searchField.setSearch(name);
        return this;
    }
}
