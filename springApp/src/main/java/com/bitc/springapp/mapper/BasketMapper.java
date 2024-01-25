package com.bitc.springapp.mapper;

import com.bitc.springapp.dto.BasketDTO;
import com.bitc.springapp.dto.TotalDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BasketMapper {

    TotalDTO SelectUserInfo(String id) throws Exception;

    String selectTotalAmount(String pdId, String pdCreateDate) throws Exception;

    List<BasketDTO> selectBasketAmountList(String pdCreateDate, String pdId) throws Exception;

    List<BasketDTO> selectBasketView(String pdId, String pdCreateDate) throws Exception;
}
