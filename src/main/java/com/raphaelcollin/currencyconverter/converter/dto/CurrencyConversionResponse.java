package com.raphaelcollin.currencyconverter.converter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Map;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyConversionResponse {
    Map<String, Map<String, String>> results;

    @JsonCreator
    public CurrencyConversionResponse(
            @JsonProperty("results") Map<String, Map<String, String>> results) {
        this.results = results;
    }

    public double getCurrencyExchange() {
        return results.values()
                .stream()
                .findFirst()
                .map(data -> data.get("val"))
                .map(Double::parseDouble)
                .orElseThrow();
    }
}
