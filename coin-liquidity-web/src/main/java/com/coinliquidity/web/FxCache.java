package com.coinliquidity.web;

import com.coinliquidity.core.fx.FxProvider;
import com.coinliquidity.core.fx.FxRates;

import java.util.List;

public class FxCache {

    private final List<FxProvider> fxProviders;

    public FxCache(final List<FxProvider> fxProviders) {
        this.fxProviders = fxProviders;
    }

    private void refresh() {
        fxProviders.forEach(FxProvider::refresh);
    }

    FxRates getRates() {

        this.refresh();

        FxRates rates = null;
        for (final FxProvider provider : fxProviders) {
            if (rates == null) {
                rates = provider.getRates();
            } else {
                rates.merge(provider.getRates());
            }
        }
        return rates;
    }
}
