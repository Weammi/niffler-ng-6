package guru.qa.niffler.page.generalForm;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class Header {

    private final SelenideElement avatarBtn = $("[aria-label='Menu']");

    public Menu clickAvatar() {
        avatarBtn.click();
        return new Menu();
    }
}
