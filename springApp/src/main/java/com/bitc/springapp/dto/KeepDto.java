package com.bitc.springapp.dto;

import lombok.Data;

@Data
public class KeepDto {
    private int kpIdx;
    private String kpCd;
    private String kdName;
    private int kdJeongGa;
    private int kdDiscount;
    private int kdPrice;
    private int kdCnt;
    private String kdCreateDate;
    private String kpId;
}
