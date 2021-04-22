package com.raphaelcollin.currencyconverter.converter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raphaelcollin.currencyconverter.converter.Currency;
import lombok.Value;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class AvailableCurrenciesResponse {
    Map<String, Map<String, String>> results;

    @JsonCreator
    public AvailableCurrenciesResponse(
            @JsonProperty("results") Map<String, Map<String, String>> results) {
        this.results = results;
    }

    public Collection<Currency> toCurrencies() {
        return results.values()
                .stream()
                .map(this::toCurrency)
                .filter(this::filterCurrency)
                .collect(Collectors.toList());
    }

    private Currency toCurrency(Map<String, String> map) {
        return new Currency(
                map.get("id"),
                map.get("currencySymbol"),
                map.get("currencyName")
        );
    }

    private boolean filterCurrency(Currency currency) {
        return currency.getId() != null && currency.getName() != null;
    }
}
