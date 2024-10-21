package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.ENTER;

public class FriendsPage {

    private final ElementsCollection friendsTable = $$("#friends tr");
    private final ElementsCollection requestsTable = $$("#requests tr");
    private final SelenideElement searchInput = $("input[type='text']");

    @Step("В таблице друзей отображается пользователь - {name}")
    public FriendsPage friendIsDisplayInFriendsList(String name) {
        friendsTable.find(text(name)).shouldBe(visible);
        return new FriendsPage();
    }

    @Step("Таблица друзей пуста")
    public FriendsPage friendTableIsEmpty() {
        friendsTable.shouldHave(size(0));
        return this;
    }

    @Step("В таблице заявок в друзья отображается пользователь - {name}")
    public FriendsPage friendIsDisplayInRequestsList(String name) {
        requestsTable.find(text(name)).shouldBe(visible);
        return this;
    }

    @Step("Ввести в поле поиска - {friendName}")
    public FriendsPage setSearch(String friendName) {
        searchInput.sendKeys(friendName);
        searchInput.sendKeys(ENTER);
        return this;
    }
}
