package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Controller
public class ExchangeRateController {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @GetMapping("/exchangeRates")
    public String getExchangeRates(Model model) {
        String apiUrl = "https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates";

        // 下載JSON文件
        try {
            downloadJson(apiUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 解析JSON文件並返回ExchangeRate對象的列表
        String jsonFilePath = "DailyForeignExchangeRates.json";
        List<ExchangeRate> exchangeRates = parseJsonToExchangeRates(jsonFilePath);

        saveExchangeRatesToDatabase(exchangeRates);

        List<String> dates = exchangeRateRepository.findAllDates();
        model.addAttribute("dates", dates);
        return "exchangeRates"; // 返回Thymeleaf模板的文件名
    }

    @PostMapping("/addExchangeRate")
    public String addExchangeRate(@RequestParam String date,
                                  @RequestParam String usdToNtd,
                                  @RequestParam String rmbToNtd,
                                  @RequestParam String usdToRmb) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setDate(date);
        exchangeRate.setUsdToNtd(usdToNtd);
        exchangeRate.setRmbToNtd(rmbToNtd);
        exchangeRate.setUsdToRmb(usdToRmb);
        exchangeRateRepository.save(exchangeRate);
        return "redirect:/exchangeRates";
    }

    @PostMapping("/updateExchangeRate")
    public String updateExchangeRate(@RequestParam String startDate,
                                     @RequestParam String usdToNtd,
                                     @RequestParam String rmbToNtd,
                                     @RequestParam String usdToRmb) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByDate(startDate);
        exchangeRate.setUsdToNtd(usdToNtd);
        exchangeRate.setRmbToNtd(rmbToNtd);
        exchangeRate.setUsdToRmb(usdToRmb);
        exchangeRateRepository.update(exchangeRate);
        return "redirect:/exchangeRates";
    }

    @PostMapping("/deleteExchangeRate")
    public String deleteExchangeRate(@RequestParam String startDate) {
        exchangeRateRepository.deleteByDate(startDate);
        return "redirect:/exchangeRates";
    }

    @GetMapping("/queryExchangeRates")
    public String queryExchangeRates(@RequestParam String startDate,
                                     Model model) {
        List<String> dates = exchangeRateRepository.findAllDates();
        model.addAttribute("dates", dates);

        ExchangeRate exchangeRates = exchangeRateRepository.findByDate(startDate);
        model.addAttribute("exchangeRates", exchangeRates);
        model.addAttribute("queryResult", exchangeRates);
        return "exchangeRates";
    }

    private void downloadJson(String url) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
        Files.write(Paths.get("DailyForeignExchangeRates.json"), response.getBody());
    }

    private List<ExchangeRate> parseJsonToExchangeRates(String jsonFilePath) {
        // 解析JSON並返回ExchangeRate對象的列表
        // 使用的是Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ExchangeRate[] exchangeRates = objectMapper.readValue(Paths.get(jsonFilePath).toFile(), ExchangeRate[].class);
            return Arrays.asList(exchangeRates);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveExchangeRatesToDatabase(List<ExchangeRate> exchangeRates) {
        for (ExchangeRate exchangeRate : exchangeRates) {
            ExchangeRate existingRate = exchangeRateRepository.findByDate(exchangeRate.getDate());
            if (existingRate == null) {
                exchangeRateRepository.save(exchangeRate);
            } else {
                existingRate.setUsdToNtd(exchangeRate.getUsdToNtd());
                existingRate.setRmbToNtd(exchangeRate.getRmbToNtd());
                existingRate.setUsdToRmb(exchangeRate.getUsdToRmb());
                exchangeRateRepository.update(existingRate);
            }
        }
    }
}
