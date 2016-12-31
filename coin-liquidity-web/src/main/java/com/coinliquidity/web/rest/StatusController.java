package com.coinliquidity.web.rest;

import com.coinliquidity.web.LiquidityCache;
import com.google.common.base.Stopwatch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/liquidity/status")
public class StatusController {

    private final LiquidityCache liquidityCache;
    private final Stopwatch stopwatch;

    public StatusController(LiquidityCache liquidityCache) {
        this.liquidityCache = liquidityCache;
        this.stopwatch = Stopwatch.createStarted();
    }

    @RequestMapping("")
    public String status(final Model model) {
        model.addAttribute("statuses", liquidityCache.getDownloadStatuses());
        model.addAttribute("uptime", stopwatch.toString());
        return "status";
    }
}
