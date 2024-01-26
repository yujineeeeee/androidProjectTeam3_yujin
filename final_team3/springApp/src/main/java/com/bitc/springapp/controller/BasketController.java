package com.bitc.springapp.controller;

import com.bitc.springapp.dto.BasketDTO;
import com.bitc.springapp.dto.TotalDTO;
import com.bitc.springapp.service.BasketService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BasketController {
    @Autowired
    private BasketService basketService;

    @GetMapping("/getUserDate")
    public TotalDTO selectUserInfo(HttpServletRequest request) throws Exception{

        String id = request.getParameter("id");
        TotalDTO user = basketService.SelectUserInfo(id);

        return user;
    }

    @PostMapping("/totalAmount")
    @ResponseBody
    public String totalAmount(@RequestBody TotalDTO total) throws Exception{
        String dailyTotal = basketService.selectTotalAmount(total.getPdId(),total.getPdCreateDate());
        return dailyTotal;
    }

    @GetMapping("/BasketAmountList")
    public List<BasketDTO> basketAmountList(HttpServletRequest request) throws Exception {
        String pdId = request.getParameter("pdId");
        String pdCreateDate = "20240119";

        List<BasketDTO> basketList = basketService.selectBasketAmountList(pdCreateDate,pdId);

        return basketList;
    }

    @GetMapping("/BasketView")
    public List<BasketDTO> BasketView(HttpServletRequest request) throws Exception{

        String pdId = request.getParameter("pdId");
        String pdCreateDate = request.getParameter("pdCreateDate");

        List<BasketDTO> basketList = basketService.selectBasketView(pdId,pdCreateDate);

        return basketList;
    }

}
