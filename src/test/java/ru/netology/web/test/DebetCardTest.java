package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DbHelper;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.PaymentPage;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DbHelper.cleanDataBase;


public class DebetCardTest {

    @BeforeEach
    void openPage() {
        cleanDataBase();
        Configuration.holdBrowserOpen = true;
        open("http://localhost:8080");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Nested
    class shouldPurchaseByCardWithDifferentStatus {
        @Test
        void purchaseWithApprovedDebetCard() {
            val validCardInformation = DataHelper.getApprovedСard();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(validCardInformation);
            paymentPage.approved();
            assertEquals("APPROVED", DbHelper.getPaymentStatusByDebetCard());
        }


        @Test
        void purchaseWithDeclinedDebetCard() {
            val invalidCardInformation = DataHelper.getDeclinedCard();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(invalidCardInformation);
            paymentPage.declined();
            assertEquals("DECLINED", DbHelper.getPaymentStatusByDebetCard());
        }
    }

    @Nested
    class FieldCardNumberTests {
        @Test
        void shouldGetNotificationEmptyFields() {
            val incorrectCardInfo = DataHelper.getCardWithEmptyFields();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldEmptyFieldNotification();
            paymentPage.shouldImproperFormatNotification();
            paymentPage.shouldValueFieldCodeCVC();
            paymentPage.shouldValueFieldYear();
            paymentPage.shouldValueFieldMonth();
            paymentPage.shouldValueFieldNumberCard();
            paymentPage.shouldValueFieldHolder();
        }

        @Test
        public void shouldShortCardNumber() {
            val incorrectCardNumber = DataHelper.getCardWithShortNumber();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardNumber);
            paymentPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldCardNumberFromZero() {
            val incorrectCardNumber = DataHelper.getCardNumberFromZero();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardNumber);
            paymentPage.declined();
        }
    }

    @Nested
    class FieldCardMonthTests {

        @Test
        public void shouldNumberMonthIMore12() {
            val incorrectCardMonth = DataHelper.getMonthIfMore12();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardMonth);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldNumberOfMonthOneDigit() {
            val incorrectCardMonth = DataHelper.getMonthWhenOneDigit();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardMonth);
            paymentPage.shouldValueFieldMonth();

        }

        @Test
        public void shouldNumberMonthFromZero() {
            val incorrectCardMonth = DataHelper.getMonthFromZero();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardMonth);
            paymentPage.declined();
            paymentPage.shouldValueFieldMonth();
        }
    }

    @Nested
    class FieldCardYearTests {
        @Test
        public void shouldInvalidYearIfZero() {
            val incorrectCardYear = DataHelper.getYearFromZero();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardYear);
            paymentPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldFutureYear() {
            val incorrectCardYear = DataHelper.getFutureYear();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardYear);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldNumberOfYearOneDigit() {
            val incorrectCardYear = DataHelper.getYearFromOneDigit();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardYear);
            paymentPage.shouldValueFieldYear();

        }

        @Test
        public void shouldPastYear() {
            val incorrectCardInfo = DataHelper.getPastYear();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldExpiredDatePassNotification();

        }
    }

    @Nested
    class FieldCardOwnerTests {

        @Test
        public void shouldCardHolderNameIsSpecialSymbols() {
            val incorrectCardHolder = DataHelper.getInvalidCardOwnerNameSpecialSymbols();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardHolder);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();

        }

        @Test
        public void shouldCardHolderNameIfRussian() {
            val incorrectCardHolder = DataHelper.getCardHolderRussianName();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardHolder);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();
        }

        @Test
        public void shouldDoubleNameOfHolder() {
            val cardHolder = DataHelper.getDoubleNameHolder();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(cardHolder);
            paymentPage.approved();
        }
    }

    @Nested
    class FieldCardCvcTests {
        @Test
        public void shouldCvcFromOneDigit() {
            val incorrectCardCvc = DataHelper.getCvcFromOneDigit();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardCvc);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromTwoDigits() {
            val incorrectCardCvc = DataHelper.getCvcFromTwoDigits();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardCvc);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromThreeZero() {
            val сardCvc = DataHelper.getCvcFromThreeZero();
            val paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(сardCvc);
            paymentPage.approved();
        }
    }
}
