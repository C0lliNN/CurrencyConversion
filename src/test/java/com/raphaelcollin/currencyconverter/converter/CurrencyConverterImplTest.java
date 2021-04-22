package com.raphaelcollin.currencyconverter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphaelcollin.currencyconverter.converter.dto.AvailableCurrenciesResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterImplTest {

    @InjectMocks
    private CurrencyConverterImpl converter;

    @Mock
    private HttpClient client;

    @Nested
    @DisplayName("method: getAvailableCurrencies()")
    class GetAvailableCurrenciesMethod {

        @AfterEach
        void tearDown() {
            verifyNoMoreInteractions(client);
        }

        @Test
        @DisplayName("when request fails, it should throw a RuntimeException")
        void whenRequestFails_shouldThrowARuntimeException() throws IOException, InterruptedException {
            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(RuntimeException.class);

            assertThrows(RuntimeException.class, () -> converter.getAvailableCurrencies());
        }

        @Test
        @DisplayName("when the response data is invalid, it should throw a RuntimeException")
        void whenTheResponseDataIsInvalid_shouldThrowARuntimeException() throws IOException, InterruptedException {
            HttpResponse<String> response = mock(HttpResponse.class);

            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(response);

            String invalidResponseBody = "This is an invalid response body";

            when(response.body()).thenReturn(invalidResponseBody);

            assertThrows(RuntimeException.class, () -> converter.getAvailableCurrencies());
        }

        @Test
        @DisplayName("when the response data is valid, it should return a collection of currencies")
        void whenTheResponseDataIsValid_shouldReturnACollectionOfCurrencies() throws IOException, InterruptedException {
            HttpResponse<String> response = mock(HttpResponse.class);

            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(response);

            Path responsePath = Paths.get("src", "test", "resources", "responses", "get-currencies-ok.json");

            String validResponseBody = Files.readString(responsePath);

            when(response.body()).thenReturn(validResponseBody);

            Collection<Currency> currencies = converter.getAvailableCurrencies();

            assertEquals(currencies.size(), 166);
        }
    }

    @Nested
    @DisplayName("method: convert(String, String, double)")
    class ConvertMethod {
        private final String SOURCE_CURRENCY = "USD";
        private final String TARGET_CURRENCY = "BRL";
        private final double VALUE = 5;

        @Test
        @DisplayName("when request fails, it should throw a RuntimeException")
        void whenRequestFails_shouldThrowARuntimeException() throws IOException, InterruptedException {
            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenThrow(RuntimeException.class);

            assertThrows(
                    RuntimeException.class,
                    () -> converter.convert(SOURCE_CURRENCY, TARGET_CURRENCY, VALUE)
            );
        }

        @Test
        @DisplayName("when the response data is invalid, it should return optional emtpy")
        void whenTheResponseDataIsInvalid_shouldThrowARuntimeException() throws IOException, InterruptedException {
            HttpResponse<String> response = mock(HttpResponse.class);

            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(response);

            String invalidResponseBody = "This is an invalid response body";

            when(response.body()).thenReturn(invalidResponseBody);

            Optional<Double> exchange = converter.convert(SOURCE_CURRENCY, TARGET_CURRENCY, VALUE);

            assertTrue(exchange.isEmpty());
        }

        @Test
        @DisplayName("when response data is valid, then it should return the exchange wrapped in Optional")
        void whenResponseDataIsValid_shouldReturnTheExchangeWrappedInOptional() throws IOException, InterruptedException {
            HttpResponse<String> response = mock(HttpResponse.class);

            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(response);

            Path responsePath = Paths.get("src", "test", "resources", "responses", "get-exchange-ok.json");
            String validResponseBody = Files.readString(responsePath);

            when(response.body()).thenReturn(validResponseBody);

            Optional<Double> exchange = converter.convert(SOURCE_CURRENCY, TARGET_CURRENCY, VALUE);

            assertTrue(exchange.isPresent());
            assertEquals(exchange.get(), 25d);
        }
    }
}