package com.zj.loglib.internal.appender;

import com.zj.loglib.model.LogEvent;

import java.util.Enumeration;
import java.util.Vector;

public class AppenderManager {

    private Vector appenderList;

    public AppenderManager() {
        appenderList = new Vector(1);
    }

    public AppenderManager(Appender appender) {
        this();
        appenderList.add(appender);
    }

    public int forceTrace(LogEvent event) {
        int size = 0;
        Appender appender;
        if (null != appenderList) {
            size = appenderList.size();
            for (int i = 0; i < size; i++) {
                appender = (Appender) appenderList.elementAt(i);
                appender.doAppend(event);
            }
        }

        return size;
    }


    public void addAppender(Appender appender) {
        if (null == appender)
            return;

        if (null == appenderList) {
            appenderList = new Vector(1);
        }

        if (!appenderList.contains(appender)) {
            appenderList.addElement(appender);
        }
    }

    public Appender getAppender(String name) {
        if (null == appenderList || null == name)
            return null;

        int size = appenderList.size();
        Appender appender;
        for (int i=0; i<size; i++) {
            appender = (Appender) appenderList.elementAt(i);
            if (name.equals(appender.getName())) {
                return appender;
            }
        }

        return null;
    }

    public Enumeration getAllAppenders() {
        if (null == appenderList)
            return null;

        return appenderList.elements();
    }
}
