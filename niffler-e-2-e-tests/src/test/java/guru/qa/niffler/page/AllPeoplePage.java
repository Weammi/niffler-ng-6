package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage extends BasePage<AllPeoplePage> {

    private final ElementsCollection allTable = $$("#all tr");

    @Step("У пользователя {name} отображается статус \"Waiting...\"")
    public AllPeoplePage friendInvitationSent(String name) {
        allTable.find(text(name)).shouldBe(visible).shouldHave(text("Waiting..."));
        return this;
    }
}