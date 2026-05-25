package ru.netology.diploma.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.diploma.data.DataHelper;
import ru.netology.diploma.data.SQLHelper;
import ru.netology.diploma.page.PageObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentTest {
    String year = LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    Faker faker = new Faker();
    DataHelper.CardInfo firstCardInfo = DataHelper.GenerateCardInformation.getFirsCardInfo(12, "en");
    DataHelper.CardInfo secondCardInfo = DataHelper.GenerateCardInformation.getSecondCardInfo(12, "en");

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("allurePayment", new AllureSelenide());
        open("http://localhost:8080/");
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allurePayment");
    }

    static void cleanCreditTables() {
        SQLHelper.clearCreditTables();
    }

    @Test
    @DisplayName("Успешная оплата первой сохраненной картой")
    void ShouldSuccessPayByFirstCard() {
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(firstCardInfo);
        payTab.pageNotification();
        var status = SQLHelper.getPaymentStatus();
        Assertions.assertEquals("APPROVED", status.getStatus());
        Assertions.assertEquals(45_000, status.getAmount());
    }

    @Test
    @DisplayName("Безуспешная оплата при попытке оплатить второй сохранненой картой")
    void ShouldNotSuccessPayBySecondCard() {
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(secondCardInfo);
        payTab.errorPageNotification();
        var status = SQLHelper.getPaymentStatus();
        Assertions.assertEquals("DECLINED", status.getStatus());
    }

    @Test
    @DisplayName("Бузуспешная оплата при попытке оплатить несохраненной картой")
    void ShouldNotSuccessPayByOtherCard() {
        var otherCardInfo = DataHelper.GenerateCardInformation.
                getOtherCardInfo(
                        12,
                        "en",
                        CreditCardType.MASTERCARD);
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(otherCardInfo);
        payTab.errorPageNotification();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при незаполненном поле номера карты")
    void ShouldDisplayErrorWhenCardFieldIsEmpty() {
        var cardWithEmptyFieldNumber = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomNumber("", 5, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyFieldNumber);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при вводе номера карты меньше 16 цифр")
    void shouldDisplayErrorWhenCardNumberContainLessNumberThenPlaceHolderFormat() {
        var otherCardInfo = DataHelper.GenerateCardInformation
                .getOtherCardInfo(36,
                        "en",
                        CreditCardType.AMERICAN_EXPRESS);
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(otherCardInfo);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при вводе номера карты больше 16 цифр")
    void shouldDisplayErrorWhenCardNumberContainUnderNumberThenPlaceHolderFormat() {
        String number = faker.number().digits(17);
        var otherCardInfo = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomNumber(number, 24, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(otherCardInfo);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при вводе цифр и букв в поле номера карты")
    void shouldDisplayErrorWhenCardNumberContainNumberAndLetters() {
        String number = faker.bothify("??##??##??##??##");
        var otherCardInfo = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomNumber(number, 24, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(otherCardInfo);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при вводе букв в поле номера карты")
    void shouldDisplayErrorWhenCardNumberContainLetters() {
        String number = faker.bothify("????????????????");
        var otherCardInfo = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomNumber(number, 24, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(otherCardInfo);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при незаполненом поле Месяц")
    void ShouldDisplayedErrorWhenMonthFieldIsEmpty() {
        var cardWithEmptyMonthDate = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomDate("", year, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyMonthDate);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при заполнении поля Месяц значением 00")
    void ShouldDisplayedErrorWhenMonthFieldIsContainZeroValue() {
        var cardWithZeroMonthDate = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomDate("00", year, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithZeroMonthDate);
        payTab.errorFieldInfoDate();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при заполнении поля Месяц значением 13")
    void ShouldDisplayedErrorWhenMonthFieldIsContainValueThirteen() {
        var cardWithThirteenMonthDate = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomDate("13", year, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithThirteenMonthDate);
        payTab.errorFieldInfoDate();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, если введена дата из прошлого")
    void ShouldDisplayedErrorWhenInputPastDate() {
        var cardWithPastDate = DataHelper.GenerateCardInformation
                .getCardInfoWithPastDate(8, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithPastDate);
        payTab.errorFieldInfoDate();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка при незаполненом поле Год")
    void ShouldDisplayedErrorWhenYearFieldIsEmpty() {
        var cardWithEmptyYearDate = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomDate("12", "", "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyYearDate);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка если введен год из далекого будущего")
    void ShouldDisplayedErrorWhenInputYearFromDistantFuture() {
        var cardWithDistantYearDate = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomDate("12", "99", "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithDistantYearDate);
        payTab.errorFieldInfoDate();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, если поле Имя не заполнено")
    void ShouldDisplayedErrorIfNameFieldIsEmpty() {
        var cardWithEmptyHolder = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomHolder("", 6, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyHolder);
        payTab.ErrorFieldHolder();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, если поле Имя содержит кириллицу")
    void ShouldDisplayedErrorIfNameFieldContainCyrillic() {
        var cardWithInvalidFormatHolder = DataHelper.GenerateCardInformation
                .getOtherCardInfo(
                        8,
                        "ru",
                        CreditCardType.MASTERCARD);
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithInvalidFormatHolder);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, если в поле Имя введены цифры")
    void ShouldDisplayedErrorIfInputNumbersInNameField() {
        String holder = faker.bothify("########");
        var cardWithInvalidFormatHolder = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomHolder(holder, 36, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithInvalidFormatHolder);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, если поле Имя содержит цифры и буквы")
    void ShouldDisplayedErrorIfNameFieldContainLettersAndNumbers() {
        String holder = faker.bothify("??##??##");
        var cardWithInvalidFormatHolder = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomHolder(holder, 36, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithInvalidFormatHolder);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, если поле Имя содержит спец символы")
    void ShouldDisplayedErrorIfNameFieldContainSpecificSymbols() {
        var cardWithInvalidFormatHolder = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomHolder("Ivan@-$Ivanov#", 36, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithInvalidFormatHolder);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, если поле CVC не заполнено")
    void ShouldDisplayedErrorIfCVCFieldIsEmpty() {
        var cardWithEmptyCVC = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomCVC("", 18, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyCVC);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, когда в поле CVC введено 2 цифры")
    void ShouldDisplayedErrorWhenInputInCVCFieldTwoNumbers() {
        String code = faker.number().digits(2);
        var cardWithEmptyCVC = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomCVC(code, 18, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyCVC);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, когда в поле CVC введено 4 цифры")
    void ShouldDisplayedErrorWhenInputInCVCFieldFourNumbers() {
        String code = faker.number().digits(4);
        var cardWithEmptyCVC = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomCVC(code, 31, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyCVC);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, когда в поле CVC введены цифры и буквы")
    void ShouldDisplayedErrorWhenInputInCVCFieldLettersAndNumbers() {
        String code = faker.bothify("#?#");
        var cardWithEmptyCVC = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomCVC(code, 31, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyCVC);
        payTab.errorFieldInfo();
    }

    @Test
    @DisplayName("Должна отобразиться ошибка, когда в поле CVC введены буквы")
    void ShouldDisplayedErrorWhenInputInCVCFieldLetters() {
        String code = faker.bothify("???");
        var cardWithEmptyCVC = DataHelper.GenerateCardInformation
                .getCardInfoWithCustomCVC(code, 31, "en");
        var payTab = new PageObject();
        payTab.setPayButton();
        payTab.paymentPageHeader();
        payTab.setField(cardWithEmptyCVC);
        payTab.errorFieldInfo();
    }
}