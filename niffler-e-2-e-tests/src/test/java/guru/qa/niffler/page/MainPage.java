package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainPage extends BasePage<MainPage> {

    public final SpendingTable spendingTable = new SpendingTable<>(this);

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement spendings = $("#spendings");
    private final SelenideElement statistics = $("#stat");
    private final SelenideElement statisticCanvas = $("canvas[role='img']");
    private final ElementsCollection statisticCells = $$("#legend-container li");

    @Step("Нажать на кнопку редактирования траты")
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("В списке отображается трата - {spendingDescription}")
    public MainPage checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
        return this;
    }

    @Step("Отображается история расходов")
    public MainPage checkHistoryOfSpendingsIsDisplay() {
        spendings.shouldBe(visible);
        return this;
    }

    @Step("Отображается статистика")
    public MainPage checkStatisticsIsDisplay() {
        statistics.shouldBe(visible);
        return this;
    }

    public MainPage deleteSpending(String desc) {
        spendingTable.deleteSpending(desc);
        return this;
    }

    @SuppressWarnings("DataFlowIssue")
    @Step("Актуальная картинка статистики равна ожидаемой картинке")
    @Nonnull
    public MainPage checkStatisticImage(BufferedImage expectedImage) throws IOException {
        BufferedImage actualImage = ImageIO.read(statisticCanvas.screenshot());
        assertFalse(new ScreenDiffResult(actualImage, expectedImage));
        return this;
    }

    @Step("Отображаются разделы {texts}")
    @Nonnull
    public MainPage checkStatisticCells(List<String> texts) {
        for (String text : texts) {
            statisticCells.findBy(text(text)).shouldBe(visible);
        }
        return this;
    }
}
