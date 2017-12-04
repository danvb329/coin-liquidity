package com.coinliquidity.web.rest;

import com.coinliquidity.web.CoinDataCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/liquidity/view/stats")
public class CoinViewController {

    private final CoinDataCache coinDataCache;

    public CoinViewController(final CoinDataCache coinDataCache) {
        this.coinDataCache = coinDataCache;
    }

    @RequestMapping("")
    public String viewAll(final Model model) {
        model.addAttribute("coinData", coinDataCache.getCoinData());
        return "stats";
    }
}
