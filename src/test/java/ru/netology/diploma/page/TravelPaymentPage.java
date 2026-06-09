package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import ru.netology.diploma.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TravelPaymentPage {
    private ElementsCollection button = $$("button");
    private ElementsCollection heading = $$("h3");
    private ElementsCollection field = $$("input.input__control");
    private ElementsCollection notification = $$("div.notification__content");
    private ElementsCollection errorField = $$("span.input__sub");

    @Step
    public void setCreditButton() {
        Allure.step("Переключение на вкладку КУПИТЬ В КРЕДИТ", () -> {
            button.find(Condition.exactText("Купить в кредит")).click();
        });
    }

    @Step
    public void setPayButton() {
        Allure.step("Переключение на вкладку КУПИТЬ", () -> {
            button.find(Condition.exactText("Купить")).click();
        });
    }

    @Step
    public void setField(DataHelper.CardInfo cardInfo) {
        Allure.step("Заполнение информации о карте", () -> {
            field.get(0).setValue(cardInfo.getNumber());
            field.get(1).setValue(cardInfo.getMonth());
            field.get(2).setValue(cardInfo.getYear());
            field.get(3).setValue(cardInfo.getHolder());
            field.get(4).setValue(cardInfo.getCode());
            button.find(Condition.exactText("Продолжить")).click();
        });
    }

    @Step
    public void paymentPageHeader() {
        Allure.step("Отобразился заголовок: Оплата по карте", () -> {
            heading.find(Condition.exactText("Оплата по карте")).shouldBe(Condition.visible);
        });
    }

    @Step
    public void creditPageHeader() {
        Allure.step("Отобразился заголовок: Кредит по данным карты", () -> {
            heading.find(Condition.exactText("Кредит по данным карты")).shouldBe(Condition.visible);
        });
    }

    @Step
    public void pageNotification() {
        Allure.step("Уведомление об успешной операции", () -> {
            notification.findBy(Condition.exactText("Операция одобрена Банком.")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        });
    }

    @Step
    public void errorPageNotification() {
        Allure.step("Уведомление об отказе в операции", () -> {
            notification.findBy(Condition.exactText("Ошибка! Банк отказал в проведении операции.")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        });
    }

    @Step
    public void errorFieldInfo() {
        Allure.step("Отобразился текст: Неверный формат ввода", () -> {
            errorField.findBy(Condition.exactText("Неверный формат")).shouldBe(Condition.visible);
        });
    }

    @Step
    public void errorFieldInfoDate() {
        Allure.step("Отобразился текст:Неверно указан либо истек срок действия карты", () -> {
            errorField.findBy(Condition.or("Срок действия",
                    Condition.exactText("Неверно указан срок действия карты"),
                    Condition.exactText("Истёк срок действия карты")
            )).shouldBe(Condition.visible);
        });
    }

    @Step
    public void ErrorFieldHolder() {
        Allure.step("Отобразился текст:Поле обязательно для заполнения", () -> {
            errorField.findBy(Condition.exactText("Поле обязательно для заполнения")).shouldBe(Condition.visible);
        });
    }
}

