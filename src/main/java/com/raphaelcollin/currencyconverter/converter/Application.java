package com.raphaelcollin.currencyconverter.converter;

import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

import static com.raphaelcollin.currencyconverter.utils.ConsoleConstants.ANSI_CYAN;
import static com.raphaelcollin.currencyconverter.utils.ConsoleConstants.ANSI_GREEN;
import static com.raphaelcollin.currencyconverter.utils.ConsoleConstants.ANSI_PURPLE;
import static com.raphaelcollin.currencyconverter.utils.ConsoleConstants.ANSI_RED;
import static com.raphaelcollin.currencyconverter.utils.ConsoleConstants.ANSI_RESET;
import static com.raphaelcollin.currencyconverter.utils.ConsoleConstants.ANSI_YELLOW;

public class Application {
    private Collection<Currency> currencies;
    private final CurrencyConverter converter;

    private static final String QUIT_OPTION = "3";

    public Application(CurrencyConverter converter) {
        this.converter = converter;
    }

    public void start() {
        this.currencies = converter.getAvailableCurrencies();

        String option;
        do {
            printHeader();
            printOptions();

            option = readOption();

            switch (option) {
                case "1":
                    printCurrencies();
                    break;
                case "2":
                    convertCurrencies();
                    break;
            }

            pause();
            cleanScreen();
        } while (!option.equals(QUIT_OPTION));
        printHeader();
    }

    private void printHeader() {
        System.out.print(ANSI_CYAN);
        System.out.println("=".repeat(80));
        System.out.println(" ".repeat(31) + "Currency Converter");
        System.out.println("=".repeat(80));
        System.out.println(ANSI_RESET);
    }

    private void printOptions() {
        System.out.println(ANSI_PURPLE);
        System.out.println("[ 1 ] List Available Currencies");
        System.out.println("[ 2 ] Convert Currencies");
        System.out.println("[ 3 ] Quit");
        System.out.print(ANSI_RESET);
    }

    private String readOption() {
        System.out.println();
        System.out.print("Enter an option: ");
        return new Scanner(System.in).nextLine();
    }

    private void printCurrencies() {
        for (Currency currency : currencies) {
            System.out.println(currency.getId() + ": " + currency.getName());
        }
    }

    private void convertCurrencies() {
        String sourceCurrencyCode = readCurrencyCode("Enter the source Currency Code (EX: USD): ");
        String targetCurrencyCode = readCurrencyCode("Enter the target Currency Code (EX: USD): ");

        double value = readValue();

        Optional<Double> result = converter.convert(sourceCurrencyCode, targetCurrencyCode, value);

        if (result.isPresent()) {
            printConversionResult(sourceCurrencyCode, targetCurrencyCode, value, result.get());
        }
    }

    private String readCurrencyCode(String message) {
        while (true) {
            try {
                System.out.print(message);
                String currencyCode = new Scanner(System.in).nextLine();

                if (isCurrencyCodeAvailable(currencyCode)) {
                    return currencyCode;
                }

                throw new Exception("Invalid Currency Code");
            } catch(Exception e) {
                System.out.println(ANSI_RED + e.getMessage() + ". Try again" + ANSI_RESET);
            }
        }
    }

    private boolean isCurrencyCodeAvailable(String currencyCode) {
        long count = currencies
                .stream()
                .map(Currency::getId)
                .filter(code -> code.equals(currencyCode))
                .count();

        return count > 0;
    }

    private double readValue() {
        while (true) {
            try {
                System.out.print("Enter the value: ");
                return new Scanner(System.in).nextDouble();
            } catch(Exception e) {
                System.out.println("Invalid value. Try again" + ANSI_RESET);
            }
        }
    }

    private void printConversionResult(String sourceCurrencyCode,
                                       String targetCurrencyCode,
                                       double value,
                                       double result) {
        System.out.println(ANSI_GREEN);
        System.out.printf("Converting from %s to %s...\n", sourceCurrencyCode, targetCurrencyCode);
        System.out.printf("%.2f = %.2f\n", value, result);
        System.out.println(ANSI_RESET);
    }

    private void pause() {
        System.out.println(ANSI_YELLOW + "Press enter to continue..." + ANSI_RESET);
        new Scanner(System.in).nextLine();
    }

    private void cleanScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
