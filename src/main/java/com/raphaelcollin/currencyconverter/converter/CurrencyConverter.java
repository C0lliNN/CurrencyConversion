package com.raphaelcollin.currencyconverter.converter;

import java.util.Collection;
import java.util.Optional;

public interface CurrencyConverter {
    Collection<Currency> getAvailableCurrencies();
    Optional<Double> convert(String sourceCurrencyId, String targetCurrencyId, double value);
}
