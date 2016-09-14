package com.coinliquidity.web.rest;

import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.LiquidityData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/liquidity/view")
public class LiquidityController {

    private final LiquidityCache liquidityCache;

    public LiquidityController(final LiquidityCache liquidityCache) {
        this.liquidityCache = liquidityCache;
    }

    @RequestMapping("/")
    public String viewAll(final Model model) {
        return liquidity(liquidityCache.getLiquidityData(), "All Pairs", model);
    }

    @RequestMapping("/currency/{baseCurrency}/{quoteCurrency}")
    public String viewByCurrencyPair(@PathVariable("baseCurrency") final String baseCurrency,
                                     @PathVariable("quoteCurrency") final String quoteCurrency,
                                     final Model model) {
        final String currencyPair = baseCurrency + "/" + quoteCurrency;
        return liquidity(liquidityCache.getLiquidityData()
                .filter(baseCurrency, quoteCurrency), "Pair: " + currencyPair, model);
    }

    @RequestMapping("/exchange/{exchange:.+}")
    public String viewByExchange(@PathVariable("exchange") final String exchange,
                                 final Model model) {
        return liquidity(liquidityCache.getLiquidityData()
                .filter(exchange), "Exchange: " + exchange, model);
    }

    private String liquidity(final LiquidityData data, final String title, final Model model) {
        model.addAttribute("title", title);
        model.addAttribute("liquidityData", data);
        return "liquidity";
    }

}
