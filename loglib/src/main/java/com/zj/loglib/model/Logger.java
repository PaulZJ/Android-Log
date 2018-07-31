package com.zj.loglib.model;

import com.zj.loglib.internal.appender.Appender;
import com.zj.loglib.internal.appender.AppenderManager;

public class Logger {

    private String name;
    private AppenderManager appenderManager;
    public String getName() {
        return name;
    }

    public void addAppender(Appender appender) {
        if (null == appenderManager) {
          appenderManager = new AppenderManager(appender);
        }else {
            appenderManager.addAppender(appender);
        }
    }

    public void trace(Object message) {
        appenderManager.forceTrace(new LogEvent("ZJ", this, message));
    }
}
