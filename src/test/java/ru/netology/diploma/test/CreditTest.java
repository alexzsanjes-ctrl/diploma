package ru.netology.diploma.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.CreditCardType;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.diploma.data.DataHelper;
import ru.netology.diploma.data.SQLHelper;
import ru.netology.diploma.page.PageObject;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

public class CreditTest {
    DataHelper.CardInfo firstCardInfo = DataHelper.GenerateCardInformation.getFirsCardInfo(12, "en");
    DataHelper.CardInfo secondCardInfo = DataHelper.GenerateCardInformation.getSecondCardInfo(12, "en");

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("allureCredit", new AllureSelenide());
        open("http://localhost:8080/");
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allureCredit");
    }
    static void cleanCreditTables() {
        SQLHelper.clearCreditTables();
    }

    @Test
    @DisplayName("Успешная покупка в кредит первой сохранненой картой")
    void ShouldSuccessCreditByFirstCard() {
        var payTab = new PageObject();
        payTab.setCreditButton();
        payTab.creditPageHeader();
        payTab.setField(firstCardInfo);
        payTab.pageNotification();
        var status = SQLHelper.getCreditStatus();
        Assertions.assertEquals("APPROVED", status.getStatus());
    }

    @Test
    @DisplayName("Бузуспешная попытка покупки в кредит второй сохраненной картой")
    void ShouldNotSuccessCreditBySecondCard() {
        var payTab = new PageObject();
        payTab.setCreditButton();
        payTab.creditPageHeader();
        payTab.setField(secondCardInfo);
        payTab.errorPageNotification();
        var status = SQLHelper.getCreditStatus();
        Assertions.assertEquals("DECLINED", status.getStatus());
    }

    @Test
    @DisplayName("Бузуспешная попытка покупки в кредит несохраненной картой")
    void ShouldNotSuccessCreditByOtherCard() {
        var payTab = new PageObject();
        var otherCardInfo = DataHelper.GenerateCardInformation
                .getOtherCardInfo(
                        3,
                        "en",
                        CreditCardType.MASTERCARD);
        payTab.setCreditButton();
        payTab.creditPageHeader();
        payTab.setField(otherCardInfo);
        payTab.errorPageNotification();
    }
}
