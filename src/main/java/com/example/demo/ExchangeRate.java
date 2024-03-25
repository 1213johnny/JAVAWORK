package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {
    // 時間
    @JsonProperty("Date")
    private String date;

    // 美金轉台幣
    @JsonProperty("USD/NTD")
    private String usdToNtd;

    // 人民幣轉台幣
    @JsonProperty("RMB/NTD")
    private String rmbToNtd;

    // 美金轉人民幣
    @JsonProperty("USD/RMB")
    private String usdToRmb;

    public String getDate() {
        return date;
    }

    public String getRmbToNtd() {
        return rmbToNtd;
    }

    public String getUsdToNtd() {
        return usdToNtd;
    }

    public String getUsdToRmb() {
        return usdToRmb;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRmbToNtd(String rmbToNtd) {
        this.rmbToNtd = rmbToNtd;
    }

    public void setUsdToNtd(String usdToNtd) {
        this.usdToNtd = usdToNtd;
    }

    public void setUsdToRmb(String usdToRmb) {
        this.usdToRmb = usdToRmb;
    }
}
