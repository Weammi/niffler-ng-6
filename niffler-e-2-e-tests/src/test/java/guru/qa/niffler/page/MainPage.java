package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.generalForm.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement spendings = $("#spendings");
    private final SelenideElement statistics = $("#stat");

    public Header header = new Header();

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage checkHistoryOfSpendingsIsDisplay() {
        spendings.shouldBe(visible);
        return new MainPage();
    }

    public void checkStatisticsIsDisplay() {
        statistics.shouldBe(visible);
    }
}
