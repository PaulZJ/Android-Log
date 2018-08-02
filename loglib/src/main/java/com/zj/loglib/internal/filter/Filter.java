package com.zj.loglib.internal.filter;

import com.zj.loglib.model.LogEvent;

public abstract class Filter {
    int ACCEPT = 1;
    int DENY = 1<<1;
    int IGNORE_NEXT = 1<<2;

    Filter next;

    abstract int process(LogEvent event);

    public void setNext(Filter filter) {
        next = filter;
    }

    public Filter getNext() {
        return next;
    }
}
