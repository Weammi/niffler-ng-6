package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {

    private final ElementsCollection friendsTable = $$("#friends tr");
    private final ElementsCollection requestsTable = $$("#requests tr");
    private final SelenideElement acceptButton = $(byText("Accept"));
    private final SelenideElement declineButton = $(byText("Decline"));
    private final SelenideElement confirmDeclineButton =
            $(".MuiPaper-root button.MuiButtonBase-root.MuiButton-containedPrimary");
    private final SelenideElement unfriendButton = $("button[class*='MuiButton-containedSecondary']");
    private final SelenideElement myFriendsListHeader = $x("//h2[text()='My friends']");
    private final SelenideElement emptyFriends = $x("//p[text()='There are no users yet']");

    public SearchField searchField = new SearchField();

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

    @Step("Ввести в поле поиска - {name}")
    public FriendsPage setSearch(String name) {
        searchField.setSearch(name);
        return this;
    }

    @Step("Принять заявку в друзья")
    public FriendsPage acceptFriend() {
        acceptButton.click();
        return this;
    }

    @Step("Отклонить заявку в друзья")
    public FriendsPage declineFriend() {
        declineButton.click();
        confirmDeclineButton.click();
        return this;
    }

    @Step("Проверить наличие кнопки 'Удалить из друзей'")
    public void checkUnfriendButtonIsVisible() {
        unfriendButton.shouldBe(visible);
    }

    @Step("Проверка отображения заголовка списка друзей")
    public FriendsPage shouldHaveMyFriendsListHeader() {
        myFriendsListHeader.shouldBe(visible);
        return this;
    }

    @Step("Проверка пустого списка друзей с сообщением: {message}")
    public FriendsPage shouldHaveEmptyFriendsTable() {
        emptyFriends.shouldBe(visible);
        return this;
    }
}
