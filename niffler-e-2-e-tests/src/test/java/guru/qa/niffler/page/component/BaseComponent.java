package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import lombok.Getter;

@Getter
public class BaseComponent<T extends BasePage<?>> {  // T должен быть подклассом BasePage

    protected final SelenideElement self;
    protected final T page;

    public BaseComponent(SelenideElement self, T page) {
        this.self = self;
        this.page = page;
    }
}
