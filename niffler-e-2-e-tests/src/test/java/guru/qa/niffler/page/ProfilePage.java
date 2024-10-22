package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    private final String muiChecked = ("Mui-checked");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveBtn = $("[type='submit']");
    private final SelenideElement showArchiveSwitch = $("span[class*='MuiSwitch-switchBase']");
    private final SelenideElement categoryInput = $("#category");
    private final ElementsCollection activeCategoryText = $$("span[class*='MuiChip-labelMedium']");
    private final ElementsCollection archiveCategoryText = $$("[class*='MuiChip-colorDefault'] span[class*='MuiChip-labelMedium']");
    private final SelenideElement successSaveChangesMessage = $x("//div[text()='Profile successfully updated']");

    public Header header = new Header();

    @Step("Ввести username - {userName}")
    public ProfilePage setUsername(String userName) {
        usernameInput.setValue(userName);
        return this;
    }

    @Step("Ввести name - {name}")
    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    @Step("Нажать кнопку \"Сохранить\"")
    public ProfilePage clickSaveBtn() {
        saveBtn.click();
        return this;
    }

    @Step("Тогл \"Show archived\" в положении ON")
    public ProfilePage checkShowArchiveOn() {
        showArchiveSwitch.shouldHave(cssClass(muiChecked));
        return this;
    }

    @Step("Тогл \"Show archived\" в положении OFF")
    public ProfilePage checkShowArchiveOff() {
        showArchiveSwitch.shouldNotHave(cssClass(muiChecked));
        return this;
    }

    @Step("Нажать кнопку \"Show archived\"")
    public ProfilePage clickShowArchive() {
        showArchiveSwitch.click();
        return this;
    }

    @Step("Добавить новую категорию с текстом - {text}")
    public ProfilePage setCategory(String text) {
        categoryInput.sendKeys(text);
        return this;
    }

    @Step("Отображается архивная категория - {textCategory}")
    public ProfilePage checkArchiveCategoryIsDisplay(String textCategory) {
        archiveCategoryText.find(text(textCategory)).shouldBe(visible);
        return this;
    }

    @Step("Отображается активная категория - {textCategory}")
    public ProfilePage checkActiveCategoryIsDisplay(String textCategory) {
        activeCategoryText.find(text(textCategory)).shouldBe(visible);
        return this;
    }


    @Step("Проверить успешное сообщение об обновлении профиля")
    public ProfilePage shouldBeVisibleSaveChangesSuccessMessage() {
        successSaveChangesMessage.shouldBe(visible);
        return this;
    }

    @Step("Проверить имя: {name}")
    public void checkName(String name) {
        nameInput.shouldHave(value(name));
    }
}
