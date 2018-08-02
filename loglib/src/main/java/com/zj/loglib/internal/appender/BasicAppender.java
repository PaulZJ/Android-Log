package com.zj.loglib.internal.appender;

import android.util.Log;

import com.zj.loglib.internal.PatternLayout;
import com.zj.loglib.internal.filter.Filter;
import com.zj.loglib.model.LogEvent;

public class BasicAppender implements Appender {
    protected PatternLayout layout;

    protected String name;

    protected Filter headFilter;
    protected Filter tailFilter;

    protected boolean closed = false;

    public BasicAppender(PatternLayout layout) {
        this.layout = layout;
    }

    @Override
    public void doAppend(LogEvent event) {
        Log.e("Logger", convert(event));
    }

    @Override
    public String convert(LogEvent event) {
        return layout.getLayout(event);
    }

    @Override
    public void addFilter(Filter filter) {
        if (headFilter == null) {
            headFilter = tailFilter = filter;
        }else {
            headFilter.setNext(filter);
            tailFilter = filter;
        }
    }

    @Override
    public Filter getFilter() {
        return headFilter;
    }

    @Override
    public void clearFilter() {
        headFilter = tailFilter = null;
    }

    @Override
    public void close() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void finalize() {
        if (this.closed)
            return;

        close();
    }
}
