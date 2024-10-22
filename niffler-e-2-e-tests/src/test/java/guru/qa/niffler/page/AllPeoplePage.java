package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {

    private final ElementsCollection allTable = $$("#all tr");

    public SearchField searchField = new SearchField();

    @Step("У пользователя {name} отображается статус \"Waiting...\"")
    public AllPeoplePage friendInvitationSent(String name) {
        allTable.find(text(name)).shouldBe(visible).shouldHave(text("Waiting..."));
        return this;
    }

    @Step("Ввести в поле поиска - {name}")
    public AllPeoplePage setSearch(String name) {
        searchField.setSearch(name);
        return this;
    }
}