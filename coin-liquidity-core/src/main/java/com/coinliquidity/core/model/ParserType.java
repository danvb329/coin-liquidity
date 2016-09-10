package com.coinliquidity.core.model;

import com.coinliquidity.core.Parser;
import com.coinliquidity.core.parsers.ArrayParser;
import com.coinliquidity.core.parsers.LabeledParser;

enum ParserType {

    ARRAY(new ArrayParser()),
    LABELED(new LabeledParser())
    ;

    private final Parser parser;

    ParserType(final Parser parser) {
        this.parser = parser;
    }

    public Parser getParser() {
        return parser;
    }
}
