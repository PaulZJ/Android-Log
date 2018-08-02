package com.zj.loglib.strategy;

import android.text.TextUtils;

import com.zj.loglib.internal.PatternLayout;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SizeLimitDailyRollingFileBaseAppender extends DailyRollingFileBaseAppender {
    private int maxFileSize = 60;
    private IRollingFileDelObserver fileDelObserver;

    public SizeLimitDailyRollingFileBaseAppender(PatternLayout layout, String filename,
                                                 String datePattern) throws IOException {
        super(layout, filename, datePattern);
    }

    /**
     * 产生新的文件分片时调用
     *
     * @throws IOException
     */
    void rollOver() throws IOException {
        super.rollOver();

        List<File> fileList = getAllLogs();
        if (null == fileList || fileList.size()==0)
            return;

        sortFiles(fileList);
        deleteOvermuch(fileList);
    }

    /**
     * 设置 文件删除 Observer
     * @param fileDelObserver
     */
    public void setFileDelObserver(IRollingFileDelObserver fileDelObserver) {
        this.fileDelObserver = fileDelObserver;
    }

    /**
     * 删除过多的文件
     *
     * @param fileList 所有日志文件
     */
    private void deleteOvermuch(List<File> fileList) {
        if (fileList.size() > maxFileSize) {
            for (int i = 0; i < fileList.size() - maxFileSize; i++) {
                if (null != fileDelObserver) {
                    fileDelObserver.onFileDelete(fileList.get(i).getName(), getFormatSize(fileList.get(i).length()));
                }
                fileList.get(i).delete();
            }
        }
    }

    /**
     * 文件大小转换
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size/1024;
        if(kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte/1024;
        if(megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte/1024;
        if(gigaByte < 1) {
            BigDecimal result2  = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte/1024;
        if(teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 根据文件名称上的特定格式的时间排序日志文件
     *
     * @param fileList
     */
    private void sortFiles(List<File> fileList) {
        Collections.sort(fileList, new Comparator<File>() {
            public int compare(File o1, File o2) {
                try {
                    if (getDateStr(o1).isEmpty()) {
                        return 1;
                    }
                    Date date1 = sdf.parse(getDateStr(o1));

                    if (getDateStr(o2).isEmpty()) {
                        return -1;
                    }
                    Date date2 = sdf.parse(getDateStr(o2));

                    if (date1.getTime() > date2.getTime()) {
                        return 1;
                    } else if (date1.getTime() < date2.getTime()) {
                        return -1;
                    }
                } catch (ParseException e) {
//                    logger.error("", e);
                }
                return 0;
            }
        });
    }

    /**
     * 获取文件名中的日期信息
     * @param file
     * @return
     */
    private String getDateStr(File file) {
        if (file == null) {
            return "null";
        }
        return file.getName().replaceAll("_" + new File(fileName).getName(), "");
    }

    /**
     * 获取所有日志文件分片，只有文件名符合DatePattern格式的才为日志文件分片
     * <br/>将过滤掉dymalic_log_xxxxx.log的日志输出文件，以及其他不合法的文件；
     * <br/>这里统计的仅仅是 2018-06-27-14-14_dymalic_log_xxxxx.log.zip 之类的日志分片文件
     *
     * @return
     */
    private List<File> getAllLogs() {
        if (TextUtils.isEmpty(fileName))
            return null;

        final File file = new File(fileName);
        File logPath = file.getParentFile();
        if (logPath == null) {
            logPath = new File(".");
        }

        File files[] = logPath.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                try {
                    sdf.parse(getDateStr(pathname));  //尝试将时间戳字符串转换为Data
                    return true;
                } catch (ParseException e) {
                    return false;   // dynamic_log_XXXXX.log 这个文件 以及其他不符合的文件名 会走到这里。
                }
            }
        });

        return files==null?null: Arrays.asList(files);
    }

    /**
     * 获取最大的文件分片数量
     * @return
     */
    public int getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * 设置最大的文件分片数量
     *
     * @param maxFileSize
     */
    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

}
