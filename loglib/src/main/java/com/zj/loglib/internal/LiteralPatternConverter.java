package com.zj.loglib.internal;

import com.zj.loglib.model.LogEvent;

public class LiteralPatternConverter extends PatternConverter {
    private String literal;

    LiteralPatternConverter(String value) {
        literal = value;
    }

    public final void format(StringBuffer buf, LogEvent event) {
        buf.append(literal);
    }

    @Override
    protected String convert(LogEvent event) {
        return literal;
    }
}
