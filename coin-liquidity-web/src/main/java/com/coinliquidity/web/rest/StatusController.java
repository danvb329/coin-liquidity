package com.coinliquidity.web.rest;

import com.coinliquidity.web.StatusCache;
import com.google.common.base.Stopwatch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/liquidity/status")
public class StatusController {

    private final StatusCache statusCache;
    private final Stopwatch stopwatch;

    public StatusController(final StatusCache statusCache) {
        this.statusCache = statusCache;
        this.stopwatch = Stopwatch.createStarted();
    }

    @RequestMapping("")
    public String status(final Model model) {
        model.addAttribute("statuses", statusCache.getDownloadStatuses());
        model.addAttribute("uptime", stopwatch.toString());
        return "status";
    }
}
