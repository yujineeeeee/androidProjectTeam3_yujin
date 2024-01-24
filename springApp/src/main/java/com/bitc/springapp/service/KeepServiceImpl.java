package com.bitc.springapp.service;

import com.bitc.springapp.dto.KeepDto;
import com.bitc.springapp.mapper.KeepMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeepServiceImpl implements KeepService {

    @Autowired
    private KeepMapper keepMapper;

    @Override
    public void keepInsert(KeepDto keep) throws Exception {
        keepMapper.keepInsert(keep);
    }
}
