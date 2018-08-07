package com.zj.loglib.internal.store;

import android.util.Log;

public class LogBuffer {

    private long ptr = 0;
    private String logPath;
    private String bufferPath;
    private int bufferSize;
    private boolean compress;

    static {
        System.loadLibrary("logger-lib");
    }

    public LogBuffer(String absoluteCacheFileName, int bufferSize, String absoluteLogFileName, boolean compress) {
        this.bufferPath = absoluteCacheFileName;
        this.bufferSize = bufferSize;
        this.logPath = absoluteLogFileName;
        this.compress = compress;
        try {
            ptr = initNative(bufferPath, bufferSize, logPath, compress);
        }catch (Exception e) {
            Log.e("LogBuffer", "LogBuffer init Error", e);
        }
    }

    public void changeLogPath(String logPath) {
        if (ptr != 0) {
            try {
                changeLogPathNative(ptr, logPath);
                this.logPath = logPath;
            }catch (Exception e) {
                Log.e("LogBuffer", "change LogPath Error", e);
            }
        }
    }

    public void release() {
        if (ptr != 0) {
            try {
                releaseNative(ptr);
            }catch (Exception e) {
                Log.e("LogBuffer", "LogBuffer release Error", e);
            }
        }
    }

    public void write(String logMessage) {
        if (ptr != 0) {
            try {
                writeNative(ptr, logMessage);
            }catch (Exception e) {
                Log.e("LogBuffer", "write Native Error", e);
            }
        }
    }

    public void flushAsync() {
        if (ptr != 0) {
            try {
                flushAsyncNative(ptr);
            }catch (Exception e) {
                Log.e("LogBuffer", "flush Async Error", e);
            }
        }
    }

    public String getLogPath() {
        return logPath;
    }

    public String getBufferPath() {
        return bufferPath;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public boolean isCompress() {
        return compress;
    }

    private native static long initNative(String bufferPath, int capacity, String logPath, boolean compress);

    private native void writeNative(long ptr, String log);

    private native void flushAsyncNative(long ptr);

    private native void releaseNative(long ptr);

    private native void changeLogPathNative(long ptr, String logPath);
}
