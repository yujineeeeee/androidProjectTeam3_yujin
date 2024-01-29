package com.bitc.springapp.service;

import com.bitc.springapp.dto.DumDTO;
import com.bitc.springapp.dto.KeepDto;

import java.util.ArrayList;
import java.util.List;

public interface KeepService {
    void keepInsert(KeepDto keep) throws Exception;

    void keepUpdate(KeepDto keep) throws Exception;

    ArrayList<DumDTO> selectItemCheck(String testId) throws Exception;

    List<KeepDto> SelectKeepView(String id) throws Exception;


    void keepCntUpdate(KeepDto keep) throws Exception;

    void keepDelete(KeepDto keep) throws Exception;

    void basketInsert(KeepDto keep) throws Exception;
}
