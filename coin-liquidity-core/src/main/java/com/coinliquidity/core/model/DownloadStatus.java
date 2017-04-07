package com.coinliquidity.core.model;

import com.google.common.base.Stopwatch;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DownloadStatus {

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    private Exchange exchange;
    private CurrencyPair currencyPair;
    private String orderBookUrl;
    private String status;
    private int sizeBytes;
    private int totalAsks;
    private int totalBids;
    private long timeElapsed;
    private LocalDateTime updateTime;
    private Stopwatch lastOk;
    private Stopwatch lastError;
    private String lastErrorMessage;
}
