package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {

    private final ElementsCollection allTable = $$("#all tr");

    public AllPeoplePage friendInvitationSent(String name) {
        allTable.find(text(name)).shouldBe(visible).shouldHave(text("Waiting..."));
        return new AllPeoplePage();
    }
}