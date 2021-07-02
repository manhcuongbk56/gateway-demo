package com.demo.httpserver.controller;

import com.demo.common.message.stockprice.GetStockPriceRequest;
import com.demo.common.message.stockprice.StockPriceResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class SimpleController {

    Random rand = new Random();


    @PostMapping("/price")
    @ResponseBody
    public StockPriceResponse getPrice(@RequestBody GetStockPriceRequest request) {
        try {
            Thread.sleep(2000L);
            int number = rand.nextInt(10);
            if (number < 1) {
                return StockPriceResponse.fail(request);
            }
            return StockPriceResponse.success(request, 20.5d);
        } catch (InterruptedException e) {
            return StockPriceResponse.fail(request);
        }

    }

}
