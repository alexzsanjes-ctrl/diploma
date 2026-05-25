package ru.netology.diploma.data;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import com.google.protobuf.StringValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {

    }

    public static String generateCardNumber(Faker faker, CreditCardType creditCardType) {
        String cardNumber = faker.finance().creditCard(creditCardType);
        return cardNumber;
    }

    public static String generateHolder(Faker faker) {
        String holder = faker.name().fullName();
        return holder;
    }

    public static String generateCode(Faker faker) {
        String code = faker.number().digits(3);
        return code;
    }

    public static String generateValidDate(int monthToAdd, String formatter) {
        String date = LocalDate.now().plusMonths(monthToAdd).format(DateTimeFormatter.ofPattern(formatter));
        return date;
    }

    public static String generateInvalidDate(int monthToSubtract, String formatter) {
        String date = LocalDate.now().minusMonths(monthToSubtract).format(DateTimeFormatter.ofPattern(formatter));
        return date;
    }

    @Data
    @NoArgsConstructor
    public static class Status {
        private String status;
        private int amount;
    }

    @Value
    public static class CardInfo {
        String number;
        String month;
        String year;
        String holder;
        String code;
    }


    public static class GenerateCardInformation {
        private static Faker faker;

        private GenerateCardInformation() {

        }

        public static CardInfo getFirsCardInfo(int monthToAdd, String locale) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    "4444 4444 4444 4441",
                    generateValidDate(monthToAdd, "MM"),
                    generateValidDate(monthToAdd, "yy"),
                    generateHolder(faker),
                    generateCode(faker));
        }

        public static CardInfo getSecondCardInfo(int monthToAdd, String locale) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    "4444 4444 4444 4442",
                    generateValidDate(monthToAdd, "MM"),
                    generateValidDate(monthToAdd, "yy"),
                    generateHolder(faker),
                    generateCode(faker));
        }

        public static CardInfo getOtherCardInfo(int monthToAdd, String locale, CreditCardType creditCardType) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    generateCardNumber(faker, creditCardType),
                    generateValidDate(monthToAdd, "MM"),
                    generateValidDate(monthToAdd, "yy"),
                    generateHolder(faker),
                    generateCode(faker));
        }

        public static CardInfo getCardInfoWithCustomNumber(String cardNumber, int monthToAdd, String locale) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    cardNumber,
                    generateValidDate(monthToAdd, "MM"),
                    generateValidDate(monthToAdd, "yy"),
                    generateHolder(faker),
                    generateCode(faker));
        }

        public static CardInfo getCardInfoWithCustomDate (String month, String year, String locale) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    "4444 4444 4444 4441",
                    month,
                    year,
                    generateHolder(faker),
                    generateCode(faker));
        }

        public static CardInfo getCardInfoWithCustomHolder(String holder, int monthToAdd, String locale) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    "4444 4444 4444 4441",
                    generateValidDate(monthToAdd, "MM"),
                    generateValidDate(monthToAdd, "yy"),
                    holder,
                    generateCode(faker));
        }

        public static CardInfo getCardInfoWithCustomCVC(String code, int monthToAdd, String locale) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    "4444 4444 4444 4441",
                    generateValidDate(monthToAdd, "MM"),
                    generateValidDate(monthToAdd, "yy"),
                    generateHolder(faker),
                    code);
        }

        public static CardInfo getCardInfoWithPastDate(int monthToSub, String locale) {
            faker = new Faker(new Locale(locale));
            return new CardInfo(
                    "4444 4444 4444 4441",
                    generateInvalidDate(monthToSub, "MM"),
                    generateInvalidDate(monthToSub, "yy"),
                    generateHolder(faker),
                    generateCode(faker));
        }
    }
}



