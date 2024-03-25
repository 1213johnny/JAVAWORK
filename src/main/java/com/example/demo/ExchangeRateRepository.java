package com.example.demo;

import java.util.List;

public interface ExchangeRateRepository {
    List<String> findAllDates();

    ExchangeRate findByDate(String date);

    void deleteByDate(String date);

    void save(ExchangeRate exchangeRate);

    void update(ExchangeRate exchangeRate);
}
