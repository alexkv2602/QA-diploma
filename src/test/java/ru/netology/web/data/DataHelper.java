package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class CardInfo {
        private String number;
        private String month;
        private String year;
        private String holder;
        private String cvc;
    }

    static Faker faker = new Faker(new Locale("en"));
    static DateTimeFormatter format = DateTimeFormatter.ofPattern("MM");
    static String monthWhenEndOfAction = LocalDate.now().plusMonths(4).format(format);
    static DateTimeFormatter formatYear = DateTimeFormatter.ofPattern("yy");
    static String yearWhenEndOfAction = LocalDate.now().plusYears(2).format(formatYear);
    static String nameHolder = faker.name().fullName();
    static String cvc = Integer.toString(faker.number().numberBetween(100, 999));

    public static CardInfo getApprovedСard() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder,
                cvc);
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("4444 4444 4444 4442", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, cvc);

    }

    public static CardInfo getCardWithEmptyFields() {
        return new CardInfo("", "", "", "", "");
    }

    public static CardInfo getCardWithShortNumber() {
        return new CardInfo("5555 5555 5555", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getCardNumberFromZero() {
        return new CardInfo("0000 0000 0000 0000", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getMonthIfMore12() {
        return new CardInfo("4444 4444 4444 4441", "19", yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getMonthWhenOneDigit() {
        return new CardInfo("4444 4444 4444 4441", "3", yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getMonthFromZero() {
        return new CardInfo("4444 4444 4444 4441", "00", yearWhenEndOfAction, nameHolder, cvc);
    }

    public static CardInfo getYearFromZero() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, "00", nameHolder, cvc);
    }

    public static CardInfo getFutureYear() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, "47", nameHolder, cvc);
    }

    public static CardInfo getYearFromOneDigit() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, "6", nameHolder, cvc);
    }

    public static CardInfo getPastYear() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, "18", nameHolder, cvc);
    }

    public static CardInfo getDoubleNameHolder() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, "Anna-Maria Petrova", cvc);
    }

    public static CardInfo getInvalidCardOwnerNameSpecialSymbols() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, "%00&0!$??", cvc);
    }

    public static CardInfo getCardHolderRussianName() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, "Екатерина Кузнецова", cvc);
    }

    public static CardInfo getCvcFromOneDigit() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, "1");
    }

    public static CardInfo getCvcFromTwoDigits() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, "98");
    }

    public static CardInfo getCvcFromThreeZero() {
        return new CardInfo("4444 4444 4444 4441", monthWhenEndOfAction, yearWhenEndOfAction, nameHolder, "000");
    }

}


