package com.zj.loglib.strategy;

import com.zj.loglib.internal.PatternLayout;
import com.zj.loglib.internal.appender.FileBaseAppender;
import com.zj.loglib.model.LogEvent;
import com.zj.loglib.utils.RollingCalendar;
import com.zj.loglib.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DailyRollingFileBaseAppender extends FileBaseAppender {
    public static final int TOP_OF_TROUBLE = -1;
    public static final int TOP_OF_MINUTE = 0;
    public static final int TOP_OF_HOUR = 1;
    public static final int HALF_DAY = 2;
    public static final int TOP_OF_DAY = 3;
    public static final int TOP_OF_WEEK = 4;
    public static final int TOP_OF_MONTH = 5;

    /**
     * 时间输出模式。
     * <p>默认模式 "'.'yyyy-MM-dd" 表示天为单位
     */
    private String datePattern = "'.'yyyy-MM-dd";

    /**
     * 下一个日志分片的名字
     */
    private String scheduledFilename;

    /**
     * 检测下一次Rolling是否发生的时间间隔
     */
    private long nextCheck = System.currentTimeMillis() - 1;

    Date now = new Date();

    SimpleDateFormat sdf;

    RollingCalendar rc = new RollingCalendar();

    // The gmtTimeZone is used only in computeCheckPeriod() method.
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    /**
     * 实例化DailyRollingFileAppender
     * <p>并打开 <code>filename</code> 的文件，Appender将输出到这个文件中。
     */
    public DailyRollingFileBaseAppender(PatternLayout layout, String filename,
                                        String datePattern) throws IOException {
        super(layout, filename, true);
        this.datePattern = datePattern;
        activateOptions();
    }

    /**
     * 设置 时间模式。格式按照{@link SimpleDateFormat}的要求
     * <p>时间模式，将影响RollOver的流程
     */
    public void setDatePattern(String pattern) {
        datePattern = pattern;
    }

    /**
     * 获取 datePattern
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * 激活时的初始化一些配置项
     */
    public void activateOptions() {
        if (datePattern != null && fileName != null) {
            now.setTime(System.currentTimeMillis());
            sdf = new SimpleDateFormat(datePattern);
            int type = computeCheckPeriod();
            printPeriodicity(type);
            rc.setType(type);
            File file = new File(fileName);

            scheduledFilename = file.getParent()+ File.separator + sdf.format(new Date(file.lastModified())) + "_" +file.getName();
        }
    }

    /**
     * 打印文件分片规则
     *
     * @param type
     */
    void printPeriodicity(int type) {
        switch (type) {
            case TOP_OF_MINUTE:
                break;
            case TOP_OF_HOUR:
                break;
            case HALF_DAY:
                break;
            case TOP_OF_DAY:
                break;
            case TOP_OF_WEEK:
                break;
            case TOP_OF_MONTH:
                break;
            default:
        }
    }


// This method computes the roll over period by looping over the
// periods, starting with the shortest, and stopping when the r0 is
// different from from r1, where r0 is the epoch formatted according
// the datePattern (supplied by the user) and r1 is the
// epoch+nextMillis(i) formatted according to datePattern. All date
// formatting is done in GMT and not local format because the test
// logic is based on comparisons relative to 1970-01-01 00:00:00
// GMT (the epoch).
    /** 计算分片规则 */
    int computeCheckPeriod() {
        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
        // set sate to 1970-01-01 00:00:00 GMT
        Date epoch = new Date(0);
        if (datePattern != null) {
            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
                simpleDateFormat.setTimeZone(gmtTimeZone); // do all date formatting in GMT
                String r0 = simpleDateFormat.format(epoch);
                rollingCalendar.setType(i);
                Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
                String r1 = simpleDateFormat.format(next);
                //System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
                if (r0 != null && r1 != null && !r0.equals(r1)) {
                    return i;
                }
            }
        }
        return TOP_OF_TROUBLE; // Deliberately head for trouble...
    }

    /**
     * 滚动当前文件到下一个文件
     */
    void rollOver() throws IOException {

        /* Compute filename, but only if datePattern is specified */
        if (datePattern == null) {
            return;
        }

        File file = new File(fileName);
        String datedFilename = file.getParent() + File.separator + sdf.format(now) + "_" + file.getName();

        // It is too early to roll over because we are still within the
        // bounds of the current interval. Rollover will occur once the
        // next interval is reached.
        if (scheduledFilename.equals(datedFilename)) {
            return;
        }

        // close current file, and rename it to datedFilename
        this.closeFile();

        File target = new File(scheduledFilename);
        if (target.exists()) {
            target.delete();
        }

        boolean result = file.renameTo(target);
        if (result) {
            ZipUtils.zipFile(String.format("%s.zip", target.getAbsolutePath()), target);
        }
        this.setFile(fileName, true, this.bufferSize);

        scheduledFilename = datedFilename;
    }

    /**
     * 日志追加函数
     * <p>DailyRollingFileAppender与其父类不同的是，在真正日志记录之前会check是否需要rollover。
     */
    public void doAppend(LogEvent event) {
        long n = System.currentTimeMillis();
        if (n >= nextCheck) {
            now.setTime(n);
            nextCheck = rc.getNextCheckMillis(now);
            try {
                rollOver();
            } catch (IOException ioe) {
                if (ioe instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        super.doAppend(event);
    }

}
