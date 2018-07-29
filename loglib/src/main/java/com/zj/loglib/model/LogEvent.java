package com.zj.loglib.model;

import java.io.Serializable;

public class LogEvent implements Serializable {

    private static final long serialVersionUID = 4286673776418910982L;
    private static long startTime = System.currentTimeMillis();

    transient public final String eventCaller;

    transient private Logger logger;

    final public String loggerName;

    transient private Object message;

    private String renderedMessage;

    private String threadName;

    public final long timeStamp;

    public LogEvent(String eventCaller, Logger logger, Object message) {
        this.eventCaller = eventCaller;
        this.logger = logger;
        this.loggerName = logger.getName();
        this.message = message;
        timeStamp = System.currentTimeMillis();
    }

    public String getRenderedMessage() {
        if (renderedMessage == null && message != null) {
            if (message instanceof String) {
                renderedMessage = (String) message;
            }else {
                renderedMessage = message.toString();
            }
        }

        return renderedMessage;
    }

    public static long getStartTime() {
        return startTime;
    }

    public String getThreadName() {
        if (threadName == null) {
            threadName = Thread.currentThread().getName();
        }

        return threadName;
    }
}
