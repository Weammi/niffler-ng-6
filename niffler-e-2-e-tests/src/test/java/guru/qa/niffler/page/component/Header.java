package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class Header {

    private final SelenideElement avatarBtn = $("[aria-label='Menu']");
    private final SelenideElement profile = $("[href='/profile']");
    private final SelenideElement friends = $("[href='/people/friends']");
    private final SelenideElement allPeople = $("[href='/people/all']");
    private final SelenideElement spending = $("[href='/spending']");

    @Step("Нажать на иконку аватара")
    public Header clickAvatar() {
        avatarBtn.click();
        return this;
    }

    @Step("Нажать кнопку \"Profile\"")
    public ProfilePage clickProfile() {
        profile.click();
        return new ProfilePage();
    }

    @Step("Нажать кнопку \"Friends\"")
    public FriendsPage clickFriends() {
        friends.click();
        return new FriendsPage();
    }

    @Step("Нажать кнопку \"All People\"")
    public AllPeoplePage clickAllPeople() {
        allPeople.click();
        return new AllPeoplePage();
    }

    @Step("Добавить новую трату")
    public EditSpendingPage clickNewSpending() {
        spending.click();
        return new EditSpendingPage();
    }
}
