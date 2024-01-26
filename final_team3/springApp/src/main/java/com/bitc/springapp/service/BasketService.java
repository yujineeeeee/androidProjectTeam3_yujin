package com.bitc.springapp.service;

import com.bitc.springapp.dto.BasketDTO;
import com.bitc.springapp.dto.TotalDTO;

import java.util.List;

public interface BasketService {

    TotalDTO SelectUserInfo(String id) throws Exception;

    String selectTotalAmount(String pdId, String pdCreateDate) throws Exception;

    List<BasketDTO> selectBasketAmountList(String pdCreateDate, String pdId) throws Exception;

    List<BasketDTO> selectBasketView(String pdId, String pdCreateDate) throws Exception;
}
