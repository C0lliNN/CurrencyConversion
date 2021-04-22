package com.raphaelcollin.currencyconverter;

import com.raphaelcollin.currencyconverter.converter.Application;
import com.raphaelcollin.currencyconverter.converter.CurrencyConverter;
import com.raphaelcollin.currencyconverter.converter.CurrencyConverterImpl;

import java.net.http.HttpClient;

public class Main {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();

        CurrencyConverter converter = new CurrencyConverterImpl(client);
        Application app = new Application(converter);

        app.start();
    }
}
