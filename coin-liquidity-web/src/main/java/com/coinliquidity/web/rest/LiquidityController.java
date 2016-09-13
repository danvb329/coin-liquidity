package com.coinliquidity.web.rest;

import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.LiquidityData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LiquidityController {

    private final LiquidityCache liquidityCache;

    public LiquidityController(final LiquidityCache liquidityCache) {
        this.liquidityCache = liquidityCache;
    }


    @RequestMapping("/liquidity/view/currency/{baseCurrency}/{quoteCurrency}")
    public String viewByCurrencyPair(@PathVariable("baseCurrency") final String baseCurrency,
                                     @PathVariable("quoteCurrency") final String quoteCurrency,
                                     final Model model) {
        final LiquidityData data = liquidityCache.getLiquidityData()
                .filter(baseCurrency, quoteCurrency);
        model.addAttribute("liquidityData", data);
        return "liquidity";
    }

    @RequestMapping("/liquidity/view/exchange/{exchange}")
    public String viewByExchange(@PathVariable("exchange") final String exchange,
                                 final Model model) {
        final LiquidityData data = liquidityCache.getLiquidityData()
                .filter(exchange);
        model.addAttribute("liquidityData", data);
        return "liquidity";
    }

}
