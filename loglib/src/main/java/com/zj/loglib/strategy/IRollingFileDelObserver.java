package com.zj.loglib.strategy;

public interface IRollingFileDelObserver {

    void onFileDelete(String fileName, String fileSize);
}
