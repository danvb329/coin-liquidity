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

    public String getDisplayClass() {
        if (ERROR.equals(status)) {
            return "status_red";
        } else if (exchange.getMaxDepth() != null &&
                (totalAsks >= exchange.getMaxDepth() || totalBids >= exchange.getMaxDepth())) {
            return "status_amber";
        } else {
            return "status_normal";
        }
    }

    public DownloadStatusKey toKey() {
        return new DownloadStatusKey(exchange.getName(), currencyPair);
    }
}
