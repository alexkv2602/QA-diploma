package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private final SelenideElement buttonBuyByDebitCard = $$(".button__text").find(exactText("Купить"));
    private final SelenideElement buttonBuyCreditCard = $$(".button__text").find(exactText("Купить в кредит"));

    public PaymentPage() {
        SelenideElement paymentBySelectedWayHeader = $$(".heading").find(exactText("Путешествие дня"));
        paymentBySelectedWayHeader.shouldBe(Condition.visible);
    }

    public DebitCardPage selectBuyByDebitCard() {
        buttonBuyByDebitCard.click();
        return new DebitCardPage();
    }

    public CreditCardPage selectBuyByCreditCard() {
        buttonBuyCreditCard.click();
        return new CreditCardPage();

    }
}

