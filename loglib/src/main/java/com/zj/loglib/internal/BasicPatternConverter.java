package com.zj.loglib.internal;

import com.zj.loglib.model.LogEvent;

public class BasicPatternConverter extends PatternConverter {
    public static final int MESSAGE_CONVERTER = 2004;
    public static final int THREAD_CONVERTER = 2002;
    public static final int RELATIVE_TIME_CONVERTER = 2000;

    int type;

    public BasicPatternConverter(FormatInfo info, int type) {
        super(info);
        this.type = type;
    }

    @Override
    protected String convert(LogEvent event) {
        switch (type) {
            case RELATIVE_TIME_CONVERTER:
                return Long.toString(event.timeStamp - LogEvent.getStartTime());
            case THREAD_CONVERTER:
                return event.getThreadName();
            case MESSAGE_CONVERTER:
                return event.getRenderedMessage();
            default:
                    return null;
        }
    }
}
