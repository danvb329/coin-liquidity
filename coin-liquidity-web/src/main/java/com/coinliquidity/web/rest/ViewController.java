package com.coinliquidity.web.rest;

import com.coinliquidity.core.analyzer.BidAskAnalyzer;
import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.ViewType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/liquidity/view")
public class ViewController {

    private final LiquidityCache cache;

    public ViewController(final LiquidityCache cache) {
        this.cache = cache;
    }

    @RequestMapping("")
    public String viewAll(final Model model) {
        return liquidity(cache.getLiquidityData(), "All Pairs", model);
    }

    @RequestMapping("/currency/{baseCurrency}/{quoteCurrency}")
    public String viewByCurrencyPair(@PathVariable("baseCurrency") final String baseCurrency,
                                     @PathVariable("quoteCurrency") final String quoteCurrency,
                                     final Model model) {
        final String currencyPair = baseCurrency + "/" + quoteCurrency;
        return liquidity(cache.getLiquidityData()
                .filter(baseCurrency, quoteCurrency), "Pair: " + currencyPair, model);
    }

    @RequestMapping("/exchange/{exchange:.+}")
    public String viewByExchange(@PathVariable("exchange") final String exchange,
                                 final Model model) {
        return liquidity(cache.getLiquidityData()
                .filter(exchange), "Exchange: " + exchange, model);
    }

    @RequestMapping("/bid-ask/{baseCurrency}")
    public String bidAsk(@PathVariable("baseCurrency") final String baseCurrency,
                          @RequestParam(value = "days", required = false, defaultValue = "10") final Integer days,
                          @RequestParam(value = "exchange", required = false, defaultValue = "all") final String exchange,
                          @RequestParam(value = "percent", required = false, defaultValue = "0") final int bidAskPercent,
                          @RequestParam(value = "view", required = false, defaultValue = "DEFAULT") final ViewType viewType,
                          final Model model) {
        cache.validateBaseCcy(baseCurrency);
        cache.validateExchange(exchange);

        model.addAttribute("baseCurrency", baseCurrency);
        model.addAttribute("days", days);
        model.addAttribute("exchange", exchange);
        model.addAttribute("percent", bidAskPercent);
        model.addAttribute("view", viewType.name());

        // for drop-downs
        model.addAttribute("currencies", cache.getBaseCurrencies());
        model.addAttribute("exchanges", cache.getExchanges());
        model.addAttribute("percents", BidAskAnalyzer.PERCENTAGES);
        model.addAttribute("views", ViewType.values());

        return "bid-ask";
    }

    private String liquidity(final LiquidityData data, final String title, final Model model) {
        model.addAttribute("title", title);
        model.addAttribute("liquidityData", data);
        return "liquidity";
    }

}
