//
// Created by 张君 on 2018/8/4.
//

#ifndef LOGGER_FLUSHBUFFER_H
#define LOGGER_FLUSHBUFFER_H

#include <sys/types.h>
#include <string.h>
#include <math.h>
#include <stdio.h>

class FlushBuffer {
public:
    FlushBuffer(FILE* log_file, size_t size = 128);
    ~FlushBuffer();
    void write(void* data, size_t len);
    void reset();
    size_t length();
    void* ptr();
    FILE* logFile();
    void releaseThis(void *release);

private:
    FILE* log_file = nullptr;
    void* release = nullptr;
    char* data_ptr = nullptr;
    char* write_ptr = nullptr;
    size_t capacity;
    size_t emptySize();
};

#endif //LOGGER_FLUSHBUFFER_H
