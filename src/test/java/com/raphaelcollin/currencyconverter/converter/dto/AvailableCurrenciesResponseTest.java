package com.raphaelcollin.currencyconverter.converter.dto;

import com.raphaelcollin.currencyconverter.converter.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AvailableCurrenciesResponseTest {

    @Nested
    @DisplayName("method: toCurrencies()")
    class ToCurrenciesMethod {

        @Test
        @DisplayName("when results are invalid, it should return an empty collection")
        void whenResultsAreInvalid_shouldThrowAnException() {
            Map<String, Map<String, String>> results = new HashMap<>();

            results.put("USD", new HashMap<>());

            var availableCurrenciesResponse = new AvailableCurrenciesResponse(results);

            assertTrue(availableCurrenciesResponse.toCurrencies().isEmpty());
        }

        @Test
        @DisplayName("when results are valid, it should return the currencies in a collection")
        void whenResultsAreValid_shouldReturnTheCurrenciesInACollection() {
            Map<String, Map<String, String>> results = new HashMap<>();

            Map<String, String> currency1 = new HashMap<>();
            currency1.put("id", "USD");
            currency1.put("currencySymbol", "$");
            currency1.put("currencyName", "Dolar");

            Map<String, String> currency2 = new HashMap<>();
            currency2.put("id", "BRL");
            currency2.put("currencySymbol", "R$");
            currency2.put("currencyName", "Real");

            results.put("USD", currency1);
            results.put("BRL", currency2);

            var availableCurrenciesResponse = new AvailableCurrenciesResponse(results);

            Currency expectedCurrency1 = new Currency("USD", "$", "Dolar");
            Currency expectedCurrency2 = new Currency("BRL", "R$", "Real");

            Collection<Currency> actualCurrencies = availableCurrenciesResponse.toCurrencies();

            assertTrue(actualCurrencies.contains(expectedCurrency1));
            assertTrue(actualCurrencies.contains(expectedCurrency2));
        }
    }

}