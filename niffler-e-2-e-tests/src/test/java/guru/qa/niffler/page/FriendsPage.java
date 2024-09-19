package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final ElementsCollection friendsTable = $$("#friends tr");
    private final ElementsCollection requestsTable = $$("#requests tr");

    public FriendsPage friendIsDisplayInFriendsList(String name) {
        friendsTable.find(text(name)).shouldBe(visible);
        return new FriendsPage();
    }

    public FriendsPage friendTableIsEmpty() {
        friendsTable.shouldHave(size(0));
        return this;
    }

    public FriendsPage friendIsDisplayInRequestsList(String name) {
        requestsTable.find(text(name)).shouldBe(visible);
        return this;
    }
}
