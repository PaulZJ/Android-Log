//
// Created by 张君 on 2018/8/4.
//

#ifndef LOGGER_ASYNCFILEFLUSH_H
#define LOGGER_ASYNCFILEFLUSH_H

#include <sys/types.h>
#include <vector>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <unistd.h>
#include "FlushBuffer.h"

class AsyncFileFlush {
public:
    AsyncFileFlush();
    ~AsyncFileFlush();
    bool async_flush(FlushBuffer* flushBuffer);
    void stopFlush();

private:
    void async_log_thread();
    ssize_t flush(FlushBuffer* flushBuffer);
    bool exit = false;
    std::vector<FlushBuffer*> async_buffer;
    std::thread async_thread;
    std::condition_variable async_condition;
    std::mutex async_mtx;
};

#endif //LOGGER_ASYNCFILEFLUSH_H
