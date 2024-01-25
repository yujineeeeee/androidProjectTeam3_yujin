package com.bitc.springapp.service;

import com.bitc.springapp.dto.BasketDTO;
import com.bitc.springapp.dto.TotalDTO;
import com.bitc.springapp.mapper.BasketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketServiceImpl implements BasketService {

    @Autowired
    private BasketMapper basketMapper;

    @Override
    public TotalDTO SelectUserInfo(String id) throws Exception {
        return basketMapper.SelectUserInfo(id);
    }

    @Override
    public String selectTotalAmount(String pdId, String pdCreateDate) throws Exception {
        return basketMapper.selectTotalAmount(pdId,pdCreateDate);
    }

    @Override
    public List<BasketDTO> selectBasketAmountList(String pdCreateDate, String pdId) throws Exception {
        return basketMapper.selectBasketAmountList(pdCreateDate,pdId);
    }

    @Override
    public List<BasketDTO> selectBasketView(String pdId, String pdCreateDate) throws Exception {
        return basketMapper.selectBasketView(pdId,pdCreateDate);
    }
}
