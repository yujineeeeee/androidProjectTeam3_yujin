package com.bitc.springapp.mapper;

import com.bitc.springapp.dto.KeepDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KeepMapper {
    void keepInsert(KeepDto keep) throws Exception;
}
