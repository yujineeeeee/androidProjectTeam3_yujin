package com.bitc.springapp.service;

import com.bitc.springapp.dto.KeepDto;

public interface KeepService {
    void keepInsert(KeepDto keep) throws Exception;
}
