package com.coinliquidity.core.model;

import com.google.common.base.Stopwatch;

import java.time.LocalDateTime;

public class DownloadStatus {

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    private String exchange;
    private CurrencyPair currencyPair;
    private String status;
    private int sizeBytes;
    private int totalAsks;
    private int totalBids;
    private long timeElapsed;
    private LocalDateTime updateTime;
    private Stopwatch lastOk;
    private Stopwatch lastError;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(CurrencyPair currencyPair) {
        this.currencyPair = currencyPair;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public int getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(int sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public int getTotalAsks() {
        return totalAsks;
    }

    public void setTotalAsks(int totalAsks) {
        this.totalAsks = totalAsks;
    }

    public int getTotalBids() {
        return totalBids;
    }

    public void setTotalBids(int totalBids) {
        this.totalBids = totalBids;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setLastOk(Stopwatch lastOk) {
        this.lastOk = lastOk;
    }

    public Stopwatch getLastOk() {
        return lastOk;
    }

    public void setLastError(Stopwatch lastError) {
        this.lastError = lastError;
    }

    public Stopwatch getLastError() {
        return lastError;
    }
}
