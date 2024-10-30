package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

public class Calendar<T extends BasePage<?>> extends BaseComponent<T> {

    public Calendar(SelenideElement self, T page) {
        super(self, page);
    }

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private final SelenideElement calendar = $("input[name='date']");

    @SuppressWarnings("unchecked")
    @Step("Выбор даты в календаре: {date}")
    public T selectDateInCalendar(Date date) {
        String formattedDate = dateFormat.format(date);

        calendar.clear();
        calendar.setValue(formattedDate);
        return getPage();
    }
}
