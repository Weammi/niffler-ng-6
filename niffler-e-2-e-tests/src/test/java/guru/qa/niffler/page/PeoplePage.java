package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage extends BasePage<PeoplePage> {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement peopleTable = $("#all");

    @Step("Отображается отправленная заявка в друзья для пользователя - {username}")
    public PeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
        friendRow.shouldHave(text("Waiting..."));
        return this;
    }
}
