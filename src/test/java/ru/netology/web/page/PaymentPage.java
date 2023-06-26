package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private final SelenideElement buttonBuyByDebitCard = $$(".button__text").find(exactText("Купить"));
    private final SelenideElement buttonBuyCreditCard = $$(".button__text").find(exactText("Купить в кредит"));
    private final SelenideElement paymentBySelectedWayHeader = $$(".heading").find(exactText("Путешествие дня"));

    public PaymentPage() {
        paymentBySelectedWayHeader.shouldBe(Condition.visible);
    }

    public DebetCardPage selectBuyByDebitCard() {
        buttonBuyByDebitCard.click();
        return new DebetCardPage();
    }

    public CreditCardPage selectBuyByCreditCard() {
        buttonBuyCreditCard.click();
        return new CreditCardPage();

    }
}

