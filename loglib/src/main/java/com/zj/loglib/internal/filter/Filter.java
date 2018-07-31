package com.zj.loglib.internal.filter;

import com.zj.loglib.model.LogEvent;

public interface Filter {
    int ACCEPT = 1;
    int DENY = 1<<1;
    int IGNORE_NEXT = 1<<2;

    Filter next = null;

    int process(LogEvent event);


}
