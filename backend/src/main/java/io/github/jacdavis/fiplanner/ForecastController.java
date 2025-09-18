package io.github.jacdavis.fiplanner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ForecastController {

    @GetMapping("/forecast")
    public Map<String, Object> forecast(
            @RequestParam int income,
            @RequestParam double savingsRate,
            @RequestParam int years) {

        // Scenario return rates
        Map<String, Double> scenarios = Map.of(
                "conservative", 0.03,
                "baseline", 0.05,
                "aggressive", 0.07
        );

        Map<String, Object> response = new HashMap<>();
        response.put("income", income);
        response.put("savingsRate", savingsRate);
        response.put("years", years);

        Map<String, Object> results = new HashMap<>();

        for (Map.Entry<String, Double> entry : scenarios.entrySet()) {
            String scenario = entry.getKey();
            double returnRate = entry.getValue();

            double balance = 0;
            List<Double> yearlyBalances = new ArrayList<>();

            for (int i = 0; i < years; i++) {
                balance = balance * (1 + returnRate) + (income * savingsRate);
                yearlyBalances.add(balance);
            }

            Map<String, Object> scenarioResult = new HashMap<>();
            scenarioResult.put("returnRate", returnRate);
            scenarioResult.put("finalBalance", balance);
            scenarioResult.put("yearlyBalances", yearlyBalances);

            results.put(scenario, scenarioResult);
        }

        response.put("scenarios", results);
        return response;
    }
}