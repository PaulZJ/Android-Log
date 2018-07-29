package com.zj.loglib.internal;

public class FormatInfo {
    int min = -1;
    int max = 0x7FFFFFFF;
    boolean leftAlign = false;

    void reset() {
        min = -1;
        max = 0x7FFFFFFF;
        leftAlign = false;
    }
}
