package com.coinliquidity.web.rest;

import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.model.LiquiditySummary;
import com.coinliquidity.web.model.ViewType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
@RequestMapping("/liquidity/data")
public class DataController {

    private final LiquidityCache cache;

    public DataController(final LiquidityCache cache) {
        this.cache = cache;
    }

    @RequestMapping("bid-ask/{baseCurrency}")
    @ResponseBody
    public Map<String, List<Object[]>> summary(
            @PathVariable("baseCurrency") final String baseCurrency,
            @RequestParam(value = "days") final int days,
            @RequestParam(value = "exchange") final String exchange,
            @RequestParam(value = "percent") final int bidAskPercent,
            @RequestParam(value = "view") final ViewType viewType) {

        cache.validateBaseCcy(baseCurrency);
        cache.validateExchange(exchange);
        cache.validateBidAskPercent(bidAskPercent);

        final List<LiquiditySummary> summaries = cache.getLiquiditySummary(
                baseCurrency,
                Instant.now().minus(days, DAYS),
                "all".equals(exchange) ? null : exchange,
                bidAskPercent,
                viewType);

        final Map<String, List<Object[]>> data = new HashMap<>();
        data.put("bids", toChartData(summaries, LiquiditySummary::getTotalBids));
        data.put("asks", toChartData(summaries, LiquiditySummary::getTotalAsks));
        data.put("price", toChartData(summaries, LiquiditySummary::getPrice));
        return data;
    }

    private List<Object[]> toChartData(final List<LiquiditySummary> summaries,
                                       final Function<LiquiditySummary, BigDecimal> function) {
        return summaries.stream()
                .map(summary -> new Object[]{summary.getUpdateTime().toEpochMilli(), function.apply(summary)})
                .collect(Collectors.toList());
    }
}
