package com.bitc.springapp.service;

import com.bitc.springapp.dto.DumDTO;
import com.bitc.springapp.dto.KeepDto;
import com.bitc.springapp.mapper.KeepMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeepServiceImpl implements KeepService {

    @Autowired
    private KeepMapper keepMapper;

    @Override
    public void keepInsert(KeepDto keep) throws Exception {
        keepMapper.keepInsert(keep);
    }


    @Override
    public void keepUpdate(KeepDto keep) throws Exception {
        keepMapper.keepUpdate(keep);
    }

    @Override
    public ArrayList<DumDTO> selectItemCheck(String testId) throws Exception {
        return keepMapper.selectItemCheck(testId);
    }

    @Override
    public List<KeepDto> SelectKeepView(String id) throws Exception {
        return keepMapper.SelectKeepView(id);
    }

    @Override
    public void keepCntUpdate(KeepDto keep) throws Exception {
        keepMapper.keepCntUpdate(keep);
    }

    @Override
    public void keepDelete(KeepDto keep) throws Exception {
        keepMapper.keepDelete(keep);
    }

    @Override
    public void basketInsert(KeepDto keep) throws Exception {

        keepMapper.basketInsert(keep);
    }
}
