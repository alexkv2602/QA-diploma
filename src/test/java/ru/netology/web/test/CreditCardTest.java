package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.DbHelper;
import ru.netology.web.page.PaymentPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.CardInfo;



public class CreditCardTest {

    @BeforeEach
    void openPage() {
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
        void shouldPurchaseWithApprovedCreditCard() throws SQLException {
            CardInfo validCardInformation = DataHelper.getApprovedCard();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(validCardInformation);
            creditCardPage.paymentApproved();
            assertEquals("APPROVED", DbHelper.getPaymentStatusByCreditCard());
        }

        @Test
        void shouldPurchaseWithDeclinedCreditCard() throws SQLException {
            var invalidCardInformation = DataHelper.getDeclinedCard();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardInformation);
            creditCardPage.paymentDeclined();
            assertEquals("DECLINED", DbHelper.getPaymentStatusByCreditCard());
        }
    }

    @Nested
    class FieldCardNumberTests {
        @Test
        void shouldNotificationEmptyFields() {
            var invalidCardInformation = DataHelper.getCardWithEmptyFields();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
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
            var invalidCardNumber = DataHelper.getCardWithShortNumber();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
            creditCardPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldNumberCardFromZero() {
            var invalidCardNumber = DataHelper.getCardNumberFromZero();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardNumber);
        }
    }

    @Nested
    class FieldMonthTests {

        @Test
        public void shouldNumberOfMonthMore12() {
            var invalidCardMonth = DataHelper.getMonthIfMore12();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardMonth);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldNumberOfMonthFromOneDigit() {
            var invalidCardMonth = DataHelper.getMonthWhenOneDigit();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardMonth);
            creditCardPage.shouldValueFieldMonth();
        }

        @Test
        public void shouldNumberOfMonthFromZero() {
            var invalidCardMonth = DataHelper.getMonthFromZero();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardMonth);

        }
    }

    @Nested
    class FieldYearTests {
        @Test
        public void shouldYearFromZero() {
            var invalidCardYear = DataHelper.getYearFromZero();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldFutureYear() {
            var invalidCardYear = DataHelper.getFutureYear();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldInvalidExpiredDateNotification();
        }

        @Test
        public void shouldNumberOfYearFromOneDigit() {
            var invalidCardYear = DataHelper.getYearFromOneDigit();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldValueFieldYear();
        }

        @Test
        public void shouldPastYear() {
            var invalidCardYear = DataHelper.getPastYear();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardYear);
            creditCardPage.shouldExpiredDatePassNotification();
        }
    }

    @Nested
    class FieldCardOwnerTests {

        @Test
        public void shouldFieldHolderFromSpecialSymbols() {
            var invalidCardHolder = DataHelper.getInvalidCardOwnerNameSpecialSymbols();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardHolder);
            creditCardPage.shouldValueFieldHolder2();
        }

        @Test
        public void shouldCardHolderNameIfRussian() {
            var invalidCardHolder = DataHelper.getCardHolderRussianName();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCardHolder);
            creditCardPage.shouldValueFieldHolder2();
        }
        @Test
        public void shouldDoubleNameOfHolder() {
            var cardHolder = DataHelper.getDoubleNameHolder();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(cardHolder);
            creditCardPage.approved();
        }
    }

    @Nested
    class FieldCardCvcTests {

        @Test
        public void shouldCvcFromOneDigit() {
            var invalidCvc = DataHelper.getCvcFromOneDigit();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCvc);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromTwoDigits() {
            var invalidCvc = DataHelper.getCvcFromTwoDigits();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(invalidCvc);
            creditCardPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromThreeZero() {
            var cardCvc = DataHelper.getCvcFromThreeZero();
            var travelPage = new PaymentPage();
            var creditCardPage = travelPage.selectBuyByCreditCard();
            creditCardPage.creditCardFullInformation(cardCvc);
            creditCardPage.approved();
        }
    }
}





