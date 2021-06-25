package com.demo.httpserver.controller;

import com.demo.httpserver.message.GetStockPriceRequest;
import com.demo.httpserver.message.StockPriceResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Random;

@Controller
public class SimpleController {

    Random rand = new Random();


    @PostMapping("/price")
    @ResponseBody
    public StockPriceResponse getPrice(@RequestBody GetStockPriceRequest request) {
        try {
            Thread.sleep(2000L);
            var number = rand.nextInt(10);
            if (number < 1) {
                return StockPriceResponse.fail(request);
            }
            return StockPriceResponse.success(request, BigDecimal.ZERO);
        } catch (InterruptedException e) {
            return StockPriceResponse.fail(request);
        }

    }

}
