package com.zj.loglib.internal;

import com.zj.loglib.model.LogEvent;

public class PatternLayout {
    public final static String LINE_SEP = System.getProperty("line.separator");
    public final static int LINE_SEP_LEN = LINE_SEP.length();

    private PatternParser patternParser;

    public PatternLayout(String pattern) {
        patternParser = new PatternParser(pattern);
    }

    public String getLayout(LogEvent event) {
        PatternConverter patternConverter = patternParser.parse();
        return patternConverter.convert(event);
    }
}
