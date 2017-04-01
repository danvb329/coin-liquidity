package com.coinliquidity.web.rest;

import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.model.LiquidityData;
import com.coinliquidity.web.model.LiquidityDatum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/liquidity/view")
public class LiquidityController {

    private final LiquidityCache liquidityCache;

    public LiquidityController(final LiquidityCache liquidityCache) {
        this.liquidityCache = liquidityCache;
    }

    @RequestMapping("")
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

    @RequestMapping("/detail/{exchange:.+}/{baseCurrency}/{quoteCurrency}")
    public String details(@PathVariable("exchange") final String exchange,
                          @PathVariable("baseCurrency") final String baseCurrency,
                          @PathVariable("quoteCurrency") final String quoteCurrency,
                          final Model model) {
        final List<LiquidityData> history = liquidityCache.getLiquidityDataHistory();
        final List<LiquidityDatum> datums = new ArrayList<>();
        for (final LiquidityData liquidityData : history) {
            final List<LiquidityDatum> temp = liquidityData.getLiquidityData().stream()
                    .filter(d -> d.matches(exchange) && d.matches(baseCurrency, quoteCurrency))
                    .collect(Collectors.toList());
            // TODO - move this elsewhere
            temp.forEach(d -> d.setUpdateTime(liquidityData.getUpdateTime()));
            datums.addAll(temp);
        }

        //datums.sort(Comparator.comparing(LiquidityDatum::getUpdateTime));

        final LiquidityData data = new LiquidityData();
        data.setLiquidityData(datums);

        // just in case
        data.filter(exchange).filter(baseCurrency, quoteCurrency);
        model.addAttribute("title", "Detail - " + exchange + " " + baseCurrency + "/" + quoteCurrency);
        model.addAttribute("liquidityData", data);
        return "detail";
    }

    @RequestMapping("/summary/{baseCurrency}")
    public String summary(@PathVariable("baseCurrency") final String baseCurrency,
                          @RequestParam(value = "days", required = false, defaultValue = "10") final Integer days,
                          final Model model) {
        model.addAttribute("baseCurrency", baseCurrency);
        model.addAttribute("days", days);
        model.addAttribute("currencies", liquidityCache.getBaseCurrencies());
        return "summary";
    }

    private String liquidity(final LiquidityData data, final String title, final Model model) {
        model.addAttribute("title", title);
        model.addAttribute("liquidityData", data);
        return "liquidity";
    }

}
