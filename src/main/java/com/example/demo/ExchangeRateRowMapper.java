package com.example.demo;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRateRowMapper implements RowMapper<ExchangeRate> {

    @Override
    public ExchangeRate mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setDate(rs.getString("date"));
        exchangeRate.setUsdToNtd(rs.getString("usd_to_ntd"));
        exchangeRate.setRmbToNtd(rs.getString("rmb_to_ntd"));
        exchangeRate.setUsdToRmb(rs.getString("usd_to_rmb"));
        return exchangeRate;
    }
}
