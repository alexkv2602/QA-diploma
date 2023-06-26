package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;
import lombok.val;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditCardPage {
    private final SelenideElement cardNumber = $(".input [placeholder='0000 0000 0000 0000']");
    private final SelenideElement month = $(".input [placeholder='08']");
    private final SelenideElement year = $(".input [placeholder='22']");
    private final SelenideElement fieldCardHolder = $$(".input__top").find(text("Владелец")).parent();
    private final SelenideElement nameCardHolder = fieldCardHolder.$(".input__control");
    private final SelenideElement cvc = $(".input [placeholder='999']");
    private final SelenideElement proceedButton = $(".form-field button");
    private final SelenideElement approvedNotification = $(".notification_status_ok");
    private final SelenideElement declinedNotification = $(".notification_status_error");
    private final SelenideElement incorrectFormat = $(byText("Неверный формат"));
    private final SelenideElement emptyField = $(byText("Поле обязательно для заполнения"));
    private final SelenideElement invalidExpiredDate = $(byText("Неверно указан срок действия карты"));
    private final SelenideElement expiredCard = $(byText("Истёк срок действия карты"));
    private final ElementsCollection resultLinks = $$(".input__top");

    public void creditCardFullInformation(DataHelper.CardInfo info) {
        cardNumber.setValue(info.getNumber());
        month.setValue(info.getMonth());
        year.setValue(info.getYear());
        nameCardHolder.setValue(info.getHolder());
        cvc.setValue(info.getCvc());
        proceedButton.click();
    }

    public void approved() {
        approvedNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void declined() {
        declinedNotification.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldValueFieldNumberCard() {
        val fieldNumberCard = resultLinks.find(text("Номер карты")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldValueFieldMonth() {
        val fieldNumberCard = resultLinks.find(text("Месяц")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));

    }

    public void shouldValueFieldYear() {
        val fieldNumberCard = resultLinks.find(text("Год")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldValueFieldCodeCVC() {
        val fieldNumberCard = resultLinks.find(text("CVC/CVV")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldValueFieldHolder() {
        val fieldNumberCard = resultLinks.find(text("Владелец")).parent();
        fieldNumberCard.shouldHave(text("Поле обязательно для заполнения"));
    }

    public void shouldValueFieldHolder2() {
        val fieldNumberCard = resultLinks.find(text("Владелец")).parent();
        fieldNumberCard.shouldHave(text("Неверный формат"));
    }

    public void shouldImproperFormatNotification() {
        incorrectFormat.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldEmptyFieldNotification() {
        emptyField.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldInvalidExpiredDateNotification() {
        invalidExpiredDate.shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void shouldExpiredDatePassNotification() {
        expiredCard.shouldBe(Condition.visible);
    }
}
