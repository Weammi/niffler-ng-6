package guru.qa.niffler.page.generalForm;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.ProfilePage;

import static com.codeborne.selenide.Selenide.$;

public class Menu {

    private final SelenideElement profile = $("[href='/profile']");
    private final SelenideElement friends = $("[href='/people/friends']");
    private final SelenideElement allPeople = $("[href='/people/all']");

    public ProfilePage clickProfile() {
        profile.click();
        return new ProfilePage();
    }

    public FriendsPage clickFriends() {
        friends.click();
        return new FriendsPage();
    }

    public AllPeoplePage clickAllPeople() {
        allPeople.click();
        return new AllPeoplePage();
    }
}
