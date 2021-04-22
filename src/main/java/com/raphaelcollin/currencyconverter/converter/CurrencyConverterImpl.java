package com.raphaelcollin.currencyconverter.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raphaelcollin.currencyconverter.converter.dto.AvailableCurrenciesResponse;
import com.raphaelcollin.currencyconverter.converter.dto.CurrencyConversionResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Optional;

import static com.raphaelcollin.currencyconverter.utils.ConsoleConstants.ANSI_RED;

@AllArgsConstructor
public class CurrencyConverterImpl implements CurrencyConverter {
    private static final String API_KEY = "ff525a1a19e46c72fc57";
    private static final String BASE_URL = "https://free.currconv.com/api/v7";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient client;

    @Override
    public Collection<Currency> getAvailableCurrencies() {
        HttpRequest request = getAvailableCurrenciesRequest();
        String rawResponse = getAvailableCurrenciesResponse(request);

        return getCurrenciesFromResponse(rawResponse);
    }

    private HttpRequest getAvailableCurrenciesRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/currencies?apiKey=" + API_KEY))
                .build();
    }

    private String getAvailableCurrenciesResponse(HttpRequest request) {
        try {
            return client
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while trying to get the currencies: " + e.getMessage());
        }
    }

    private Collection<Currency> getCurrenciesFromResponse(String response) {
        try {
            var currenciesResponse = objectMapper.readValue(response, AvailableCurrenciesResponse.class);

            return currenciesResponse.toCurrencies();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while trying to parse JSON response: " + e.getMessage());
        }
    }

    @Override
    public Optional<Double> convert(String sourceCurrencyId, String targetCurrencyId, double value) {
        HttpRequest request = getConvertCurrenciesRequest(sourceCurrencyId, targetCurrencyId);
        String rawResponse = getConvertCurrenciesResponse(request);

        return getCurrencyExchangeFromResponse(rawResponse).map(exchange -> exchange * value);
    }

    private HttpRequest getConvertCurrenciesRequest(String sourceCurrencyId, String targetCurrencyId) {
        String endpoint = getConvertCurrenciesEndpoint(sourceCurrencyId, targetCurrencyId);

        return HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .build();
    }

    private String getConvertCurrenciesEndpoint(String sourceCurrencyId, String targetCurrencyId) {
        return String.format("%s/convert?q=%s_%s&apiKey=%s", BASE_URL, sourceCurrencyId, targetCurrencyId, API_KEY);
    }

    private String getConvertCurrenciesResponse(HttpRequest request) {
        try {
            return client
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while trying to convert the currencies: " + e.getMessage());
        }
    }

    private Optional<Double> getCurrencyExchangeFromResponse(String response) {
        try {
            var currencyConversionResponse = objectMapper.readValue(response, CurrencyConversionResponse.class);
            return Optional.of(currencyConversionResponse.getCurrencyExchange());
        } catch (JsonProcessingException e) {
            System.out.println(ANSI_RED + "Error trying to parse convert response");
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
