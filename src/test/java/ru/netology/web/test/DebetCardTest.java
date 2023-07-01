package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DbHelper;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.PaymentPage;


import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DbHelper.cleanDataBase;


public class DebetCardTest {

    @BeforeEach
    void openPage() throws SQLException {
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
            var validCardInformation = DataHelper.getApprovedСard();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(validCardInformation);
            paymentPage.approved();
            assertEquals("APPROVED", DbHelper.getPaymentStatusByDebetCard());
        }


        @Test
        void purchaseWithDeclinedDebetCard() {
            var invalidCardInformation = DataHelper.getDeclinedCard();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(invalidCardInformation);
            paymentPage.declined();
            assertEquals("DECLINED", DbHelper.getPaymentStatusByDebetCard());
        }
    }

    @Nested
    class FieldCardNumberTests {
        @Test
        void shouldGetNotificationEmptyFields() {
            var incorrectCardInfo = DataHelper.getCardWithEmptyFields();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
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
            var incorrectCardNumber = DataHelper.getCardWithShortNumber();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardNumber);
            paymentPage.shouldValueFieldNumberCard();
        }

        @Test
        public void shouldCardNumberFromZero() {
            var incorrectCardNumber = DataHelper.getCardNumberFromZero();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardNumber);
            paymentPage.declined();
        }
    }

    @Nested
    class FieldCardMonthTests {

        @Test
        public void shouldNumberMonthIMore12() {
            var incorrectCardMonth = DataHelper.getMonthIfMore12();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardMonth);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldNumberOfMonthOneDigit() {
            var incorrectCardMonth = DataHelper.getMonthWhenOneDigit();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardMonth);
            paymentPage.shouldValueFieldMonth();

        }

        @Test
        public void shouldNumberMonthFromZero() {
            var incorrectCardMonth = DataHelper.getMonthFromZero();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardMonth);
            paymentPage.declined();
            paymentPage.shouldValueFieldMonth();
        }
    }

    @Nested
    class FieldCardYearTests {
        @Test
        public void shouldInvalidYearIfZero() {
            var incorrectCardYear = DataHelper.getYearFromZero();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardYear);
            paymentPage.shouldExpiredDatePassNotification();
        }

        @Test
        public void shouldFutureYear() {
            var incorrectCardYear = DataHelper.getFutureYear();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardYear);
            paymentPage.shouldInvalidExpiredDateNotification();

        }

        @Test
        public void shouldNumberOfYearOneDigit() {
            var incorrectCardYear = DataHelper.getYearFromOneDigit();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardYear);
            paymentPage.shouldValueFieldYear();

        }

        @Test
        public void shouldPastYear() {
            var incorrectCardInfo = DataHelper.getPastYear();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardInfo);
            paymentPage.shouldExpiredDatePassNotification();

        }
    }

    @Nested
    class FieldCardOwnerTests {

        @Test
        public void shouldCardHolderNameIsSpecialSymbols() {
            var incorrectCardHolder = DataHelper.getInvalidCardOwnerNameSpecialSymbols();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardHolder);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();

        }

        @Test
        public void shouldCardHolderNameIfRussian() {
            var incorrectCardHolder = DataHelper.getCardHolderRussianName();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardHolder);
            paymentPage.declined();
            paymentPage.shouldValueFieldHolder2();
        }

        @Test
        public void shouldDoubleNameOfHolder() {
            var cardHolder = DataHelper.getDoubleNameHolder();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(cardHolder);
            paymentPage.approved();
        }
    }

    @Nested
    class FieldCardCvcTests {
        @Test
        public void shouldCvcFromOneDigit() {
            var incorrectCardCvc = DataHelper.getCvcFromOneDigit();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardCvc);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromTwoDigits() {
            var incorrectCardCvc = DataHelper.getCvcFromTwoDigits();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(incorrectCardCvc);
            paymentPage.shouldValueFieldCodeCVC();
        }

        @Test
        public void shouldCvcFromThreeZero() {
            var сardCvc = DataHelper.getCvcFromThreeZero();
            var paymentPage = new PaymentPage().selectBuyByDebitCard();
            paymentPage.fillCardInformationForSelectedWay(сardCvc);
            paymentPage.approved();
        }
    }
}
