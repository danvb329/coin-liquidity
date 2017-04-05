package com.coinliquidity.core.fx;

public interface FxProvider {
    FxRates getRates();

    default void refresh() {
        // do nothing
    }
}
