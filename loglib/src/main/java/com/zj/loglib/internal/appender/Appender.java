package com.zj.loglib.internal.appender;

import com.zj.loglib.internal.filter.Filter;
import com.zj.loglib.model.LogEvent;

public interface Appender {

    void doAppend(LogEvent event);

    String convert(LogEvent event);

    void addFilter(Filter filter);

    Filter getFilter();

    void clearFilter();

    void close();

    String getName();

    void setName(String name);
}
