package com.zj.loglib.internal.appender;

import com.zj.loglib.model.LogEvent;

import java.util.LinkedList;

public class AppenderManager {

    private LinkedList<Appender> aai;

    public AppenderManager() {
        aai = new LinkedList<>();
    }

    public AppenderManager(Appender appender) {
        this();
        aai.add(appender);
    }

    public void forceTrace(LogEvent event) {
        for (Appender appender: aai) {
            appender.doAppend(event);
        }
    }


    public void addAppender(Appender appender) {
        aai.add(appender);
    }

    public void removeAppender(Appender appender) {
        aai.remove(appender);
    }

    public int getAppenderSize() {
        return aai.size();
    }
}
