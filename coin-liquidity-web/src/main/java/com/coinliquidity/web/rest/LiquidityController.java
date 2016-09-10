package com.coinliquidity.web.rest;

import com.coinliquidity.web.LiquidityCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LiquidityController {

    private final LiquidityCache liquidityCache;

    public LiquidityController(final LiquidityCache liquidityCache) {
        this.liquidityCache = liquidityCache;
    }


    @RequestMapping("/liquidity/100k")
    public String liquidity100k(final Model model) {
        calculateLiquidity(model);
        return "liquidity";
    }

    private void calculateLiquidity(final Model model) {
        model.addAttribute("liquidityData", liquidityCache.getLiquidityData());
    }

}
