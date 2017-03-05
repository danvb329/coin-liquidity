package com.coinliquidity.web.rest;

import com.coinliquidity.web.LiquidityCache;
import com.coinliquidity.web.model.LiquiditySummary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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

    @RequestMapping("summary/asks/{baseCurrency}")
    @ResponseBody
    public List<Object[]> asks(@PathVariable("baseCurrency") final String baseCurrency,
                               @RequestParam(value = "days", required = false, defaultValue = "10") final Integer days) {
        return fetchSummary(baseCurrency, LiquiditySummary::getTotalAsks, days);
    }

    @RequestMapping("summary/bids/{baseCurrency}")
    @ResponseBody
    public List<Object[]> bids(@PathVariable("baseCurrency") final String baseCurrency,
                               @RequestParam(value = "days", required = false, defaultValue = "10") final Integer days) {
        return fetchSummary(baseCurrency, LiquiditySummary::getTotalBidsUsd, days);
    }

    private List<Object[]> fetchSummary(final String baseCurrency,
                                        final Function<LiquiditySummary, BigDecimal> function,
                                        final Integer days) {
        final List<LiquiditySummary> summaries = cache.getLiquiditySummary(
                baseCurrency,
                Instant.now().minus(days, DAYS));

        return summaries.stream()
                .map(summary -> new Object[]{summary.getUpdateTime().toEpochMilli(), function.apply(summary)})
                .collect(Collectors.toList());
    }
}
