package com.zj.loglib.internal.appender;

import com.zj.loglib.model.LogEvent;

public interface Appender {

    void doAppend(LogEvent event);

    String convert(LogEvent event);

}
