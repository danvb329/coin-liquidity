package com.coinliquidity.core.analyzer;

import com.coinliquidity.core.model.OrderBook;

public interface Analyzer {

    void analyze(final OrderBook orderBook);
}
