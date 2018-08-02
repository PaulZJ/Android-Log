package com.zj.loglib.internal.appender;

import com.zj.loglib.internal.PatternLayout;
import com.zj.loglib.internal.store.LogBuffer;
import com.zj.loglib.model.LogEvent;

import java.io.File;

public class FileBaseAppender extends BasicAppender {
    protected boolean fileAppend = true;

    protected String fileName = null;

    protected int bufferSize = 8 * 1024;

    private LogBuffer logBuffer;

    public FileBaseAppender(PatternLayout layout, String fileName, boolean append) {
        super(layout);
    }

    public synchronized void setFile(String fileName, boolean append, int bufferSize) {
        this.fileName = fileName;
        this.fileAppend = append;
        this.bufferSize = bufferSize;

        File tmp = new File(fileName);
        File cacheFile = new File(tmp.getParent(), "buffer.cache");
        if (null != logBuffer) {
            logBuffer.release();
        }

        logBuffer = new LogBuffer(cacheFile.getAbsolutePath(), bufferSize, tmp.getAbsolutePath(), false);
    }

    @Override
    public void doAppend(LogEvent event) {
        logBuffer.write(this.layout.getLayout(event));
    }

    @Override
    public void close() {
        if (null != logBuffer) {
            logBuffer.release();
        }
        fileName = null;
    }

    public void closeFile() {
        close();
    }
}
