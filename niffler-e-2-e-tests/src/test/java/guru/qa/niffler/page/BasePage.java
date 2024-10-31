package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.Search;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    @Getter
    protected final Header<T> header;
    @Getter
    protected final Calendar<T> calendar;
    @Getter
    protected final Search<T> search;

    protected final SelenideElement alert = $("div[class*='MuiAlert-standard']");

    @SuppressWarnings("unchecked")
    public BasePage() {
        this.header = new Header<>($("#root header"), (T) this);
        this.calendar = new Calendar<>($("div[data-popper-placement='bottom-start']"), (T) this);
        this.search = new Search<>($("input[placeholder='Search']"), (T) this);
    }

    @SuppressWarnings("unchecked")
    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        return (T) this;
    }
}
