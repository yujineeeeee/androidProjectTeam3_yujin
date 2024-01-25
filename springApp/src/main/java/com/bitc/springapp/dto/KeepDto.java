package com.bitc.springapp.dto;

import lombok.Data;

@Data
public class KeepDto {
    private int kpIdx;
    private String kpCd;
    private String kpName;
    private int kpJeongGa;
    private int kpDiscount;
    private int kpPrice;
    private int kpCnt;
    private String kpCreateDate;
    private String kpId;
    private String kpImage;
}
