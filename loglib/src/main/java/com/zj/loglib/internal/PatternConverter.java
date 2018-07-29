package com.zj.loglib.internal;

import com.zj.loglib.model.LogEvent;

public abstract class PatternConverter {
    public PatternConverter next;
    int min = -1;
    int max = 0x7FFFFFFF;
    boolean leftAlign = false;

    static String[] SPACES = {" ", "  ", "    ", "        ",    //1, 2, 4, 8 spaces
        "                ", // 16 spaces
        "                                "};    //32 spaces

    PatternConverter() {

    }

    protected PatternConverter(FormatInfo info) {
        min = info.min;
        max = info.max;
        leftAlign = info.leftAlign;
    }

    abstract protected String convert(LogEvent event);

    public void format(StringBuffer buf, LogEvent event) {
        String eventString = convert(event);

        if (eventString == null) {
            if (0 < min) {
                spacePad(buf, min);
                return;
            }
        }

        int len = eventString.length();

        if (len > max) {
            buf.append(eventString.substring(len - max));
        }else if (len < min) {
            if (leftAlign) {
                buf.append(eventString);
                spacePad(buf, min - len);
            }else {
                spacePad(buf, min - len);
                buf.append(eventString);
            }
        }else {
            buf.append(eventString);
        }
    }

    public void spacePad(StringBuffer sb, int length) {
        while (length >= 32) {
            sb.append(SPACES[5]);
            length -= 32;
        }

        for (int i = 4; i >=0; i--) {
            if ((length & (1<<i)) != 0) {
                sb.append(SPACES[i]);
            }
        }
    }

}
