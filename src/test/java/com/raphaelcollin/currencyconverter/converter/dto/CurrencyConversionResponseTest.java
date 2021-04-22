package com.raphaelcollin.currencyconverter.converter.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConversionResponseTest {

    @Nested
    @DisplayName("method: getCurrencyExchange()")
    class GetCurrencyExchangeMethod {

        @Test
        @DisplayName("when results are empty, it should throw an exception")
        void whenResultsAreEmpty_shouldThrowAnException() {
            Map<String, Map<String, String>> results = new HashMap<>();

            var currencyConversionResponse = new CurrencyConversionResponse(results);

            assertThrows(Exception.class, currencyConversionResponse::getCurrencyExchange);
        }

        @Test
        @DisplayName("when results contains invalid 'val', it should throw an exception")
        void whenResultsContainsInvalidVal_shouldThrowAnException() {
            Map<String, Map<String, String>> results = new HashMap<>();

            Map<String, String> result = new HashMap<>();
            result.put("val", "test");

            results.put("result1", result);

            var currencyConversionResponse = new CurrencyConversionResponse(results);

            assertThrows(Exception.class, currencyConversionResponse::getCurrencyExchange);
        }

        @Test
        @DisplayName("when results contains a valid 'val', it should return the exchange")
        void whenResultsContainsAValidVal_shouldReturnTheExchange() {
            Map<String, Map<String, String>> results = new HashMap<>();

            Map<String, String> result = new HashMap<>();
            result.put("val", "5.2378");

            results.put("result1", result);

            var currencyConversionResponse = new CurrencyConversionResponse(results);

            assertEquals(currencyConversionResponse.getCurrencyExchange(), 5.2378);
        }
    }
}