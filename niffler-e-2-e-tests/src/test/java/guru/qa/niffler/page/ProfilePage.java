package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.generalForm.Header;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final String muiChecked = ("Mui-checked");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveBtn = $("[type='submit']");
    private final SelenideElement showArchiveSwitch = $("span[class*='MuiSwitch-switchBase']");
    private final SelenideElement categoryInput = $("#category");
    private final ElementsCollection activeCategoryText = $$("span[class*='MuiChip-labelMedium']");
    private final ElementsCollection archiveCategoryText = $$("[class*='MuiChip-colorDefault'] span[class*='MuiChip-labelMedium']");

    public Header header = new Header();

    public ProfilePage setUsername(String userName) {
        usernameInput.clear();
        usernameInput.setValue(userName);
        return new ProfilePage();
    }

    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return new ProfilePage();
    }

    public ProfilePage clickSaveBtn() {
        saveBtn.click();
        return new ProfilePage();
    }

    public ProfilePage checkShowArchiveOn() {
        showArchiveSwitch.shouldHave(cssClass(muiChecked));
        return new ProfilePage();
    }

    public ProfilePage checkShowArchiveOff() {
        showArchiveSwitch.shouldNotHave(cssClass(muiChecked));
        return new ProfilePage();
    }

    public ProfilePage clickShowArchive() {
        showArchiveSwitch.click();
        return new ProfilePage();
    }

    public ProfilePage setCategory(String text) {
        categoryInput.sendKeys(text);
        return new ProfilePage();
    }

    public ProfilePage checkArchiveCategoryIsDisplay(String textCategory) {
        archiveCategoryText.find(text(textCategory)).shouldBe(visible);
        return new ProfilePage();
    }

    public ProfilePage checkActiveCategoryIsDisplay(String textCategory) {
        activeCategoryText.find(text(textCategory)).shouldBe(visible);
        return new ProfilePage();
    }
}