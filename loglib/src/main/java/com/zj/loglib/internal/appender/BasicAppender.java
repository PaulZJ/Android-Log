package com.zj.loglib.internal.appender;

import android.util.Log;

import com.zj.loglib.internal.PatternLayout;
import com.zj.loglib.model.LogEvent;

public class BasicAppender implements Appender {
    private PatternLayout layout;

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
}
