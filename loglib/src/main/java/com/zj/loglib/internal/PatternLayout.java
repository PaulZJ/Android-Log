package com.zj.loglib.internal;

import com.zj.loglib.model.LogEvent;

public class PatternLayout {
    public final static String LINE_SEP = System.getProperty("line.separator");
    public final static int LINE_SEP_LEN = LINE_SEP.length();

    public final static String DEFAULT_CONVERSION_PATTERN = "%m%n";

    protected final int BUF_SIZE = 256;
    protected final int MAX_CAPACITY = 1024;

    private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

    private PatternParser patternParser;
    private PatternConverter patternConverter;

    public PatternLayout() {
        this(DEFAULT_CONVERSION_PATTERN);
    }

    public PatternLayout(String pattern) {
        patternParser = new PatternParser(pattern);
        patternConverter = patternParser.parse();
    }

    public String getLayout(LogEvent event) {
        return patternConverter.convert(event);
    }

    public String format(LogEvent event) {
        if (sbuf.capacity() > MAX_CAPACITY) {
            sbuf = new StringBuffer(BUF_SIZE);
        }else {
            sbuf.setLength(0);
        }

        while (patternConverter != null) {
            patternConverter.format(sbuf, event);
            patternConverter = patternConverter.next;
        }

        return sbuf.toString();

    }
}
