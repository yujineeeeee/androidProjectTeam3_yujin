package com.bitc.springapp.mapper;

import com.bitc.springapp.dto.DumDTO;
import com.bitc.springapp.dto.KeepDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface KeepMapper {
    void keepInsert(KeepDto keep) throws Exception;


    void keepUpdate(KeepDto keep) throws Exception;

    ArrayList<DumDTO> selectItemCheck(String testId) throws Exception;

    List<KeepDto> SelectKeepView(@Param("kp_id") String id) throws Exception;

    void keepCntUpdate(KeepDto keep) throws Exception;

    void keepDelete(KeepDto keep) throws Exception;

    void basketInsert(KeepDto keep) throws Exception;
}
