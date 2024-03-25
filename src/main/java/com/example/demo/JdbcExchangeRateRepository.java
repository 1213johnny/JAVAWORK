package com.example.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcExchangeRateRepository implements ExchangeRateRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcExchangeRateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> findAllDates() {
        String sql = "SELECT DISTINCT date FROM exchange_rate";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public ExchangeRate findByDate(String date) {
        String sql = "SELECT * FROM exchange_rate WHERE date = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{date}, new ExchangeRateRowMapper());
    }

    @Override
    public void deleteByDate(String date) {
        String sql = "DELETE FROM exchange_rate WHERE date = ?";
        jdbcTemplate.update(sql, date);
    }

    @Override
    public void save(ExchangeRate exchangeRate) {
        String sql = "INSERT INTO exchange_rate (date, usd_to_ntd, rmb_to_ntd, usd_to_rmb) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, exchangeRate.getDate(), exchangeRate.getUsdToNtd(), exchangeRate.getRmbToNtd(), exchangeRate.getUsdToRmb());
    }

    @Override
    public void update(ExchangeRate exchangeRate) {
        String sql = "UPDATE exchange_rate SET usd_to_ntd = ?, rmb_to_ntd = ?, usd_to_rmb = ? WHERE date = ?";
        jdbcTemplate.update(sql, exchangeRate.getUsdToNtd(), exchangeRate.getRmbToNtd(), exchangeRate.getUsdToRmb(), exchangeRate.getDate());
    }
}
