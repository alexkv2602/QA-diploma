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


public class CreditCardTest {

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
    class PurchaseByCardWithDifferentStatus {
        @Test
        void shouldPurсhaseWithApprovedCreditCard() {
            final DataHelper.CardInfo validCardInformation = DataHelper.getApprovedСard();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(validCardInformation);
            creditCardPage.approved();
            assertEquals("APPROVED", DbHelper.getPaymentStatusByCreditCard());
        }

        @Test
        void shouldPurchaseWithDeclinedCreditCard() {
            val invalidCardInformation = DataHelper.getDeclinedCard();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInformation);
            creditCardPage.declined();
            assertEquals("DECLINED", DbHelper.getPaymentStatusByCreditCard());
        }
    }

    @Nested
    class FieldCardNumberTests {
        @Test
        void shouldNotificationEmptyFields() {
            val invalidCardInformation = DataHelper.getCardWithEmptyFields();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInformation);
            creditCardPage.shouldEmptyFieldNotification();
            creditCardPage.shouldImproperFormatNotification();
            creditCardPage.shouldValueFieldCodeCVC();
            creditCardPage.shouldValueFieldHolder();
            creditCardPage.shouldValueFieldMonth();
            creditCardPage.shouldValueFieldYear();
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldFieldNumberCardShortNumber() {
            val invalidCardNumber = DataHelper.getCardWithShortNumber();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldNumberCardFromZero() {
            val invalidCardNumber = DataHelper.getCardNumberFromZero();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.declined();
        }
    }

    @Nested
    class FieldMonthTests {

        @Test
        public void shouldNumberOfMonthMore12() {
            val invalidCardMonth = DataHelper.getMonthIfMore12();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardMonth);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldNumberOfMonthFromOneDigit() {
            val invalidCardMonth = DataHelper.getMonthWhenOneDigit();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardMonth);
            creditCardPage.shouldValueFieldMonth();
        }

        @Test
        public void shouldNumberOfMonthFromZero() {
            val invalidCardMonth = DataHelper.getMonthFromZero();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardMonth);
            creditCardPage.declined();
        }
    }

    @Nested
    class FieldYearTests {
        @Test
        public void shouldYearFromZero() {
            val invalidCardYear = DataHelper.getYearFromZero();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldFutureYear() {
            val invalidCardYear = DataHelper.getFutureYear();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldNumberOfYearFromOneDigit() {
            val invalidCardYear = DataHelper.getYearFromOneDigit();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldValueFieldYear();
        }

        @Test
        public void shouldPastYear() {
            val invalidCardYear = DataHelper.getPastYear();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldExpiredDatePassNotification();
        }
    }

    @Nested
    class FieldCardOwnerTests {

        @Test
        public void shouldFieldHolderFromSpecialSymbols() {
            val invalidCardHolder = DataHelper.getInvalidCardOwnerNameSpecialSymbols();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardHolder);
            creditCardPage.shouldValueFieldHolder2();
        }

        @Test
        public void shouldCardHolderNameIfRussian() {
            val invalidCardHolder = DataHelper.getCardHolderRussianName();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardHolder);
            creditCardPage.shouldValueFieldHolder2();
        }
        @Test
        public void shouldDoubleNameOfHolder() {
            val cardHolder = DataHelper.getDoubleNameHolder();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(cardHolder);
            creditCardPage.approved();
        }
    }

    @Nested
    class FieldCardCvcTests {

        @Test
        public void shouldCvcFromOneDigit() {
            val invalidCvc = DataHelper.getCvcFromOneDigit();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCvc);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromTwoDigits() {
            val invalidCvc = DataHelper.getCvcFromTwoDigits();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCvc);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromThreeZero() {
            val cardCvc = DataHelper.getCvcFromThreeZero();
            val travelPage = new PaymentPage();
            val creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(cardCvc);
            creditCardPage.approved();
        }
    }
}





