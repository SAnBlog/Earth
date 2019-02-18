package cn.sanii.earth.util;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: shouliang.wang
 * @Date: 2018/9/30 09:55
 * @Description: guava线程池工具类
 */
@Slf4j
public class GuavaThreadPoolUtils {
    /**
     * 禁止初始化
     */
    private GuavaThreadPoolUtils() {
    }

    //当前可用CPU数
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();


    /**
     * 创建线程池
     *
     * @return
     */
    private static ListeningExecutorService doGuavaExecutor(int coreSize, int maxSize, int queueSize, final String threadName, long timeOut) {
        ThreadPoolExecutor executor = doThreadPoolExecutor(coreSize, maxSize, queueSize, threadName, timeOut);
        return MoreExecutors.listeningDecorator(executor);
    }

    /**
     * 得到一个基本线程池
     *
     * @param coreSize
     * @param maxSize
     * @param queueSize
     * @param threadName
     * @param timeOut
     * @return
     */
    private static ThreadPoolExecutor doThreadPoolExecutor(int coreSize, int maxSize, int queueSize, String threadName, long timeOut) {
        //线程名
        String threadNameStr = new StringBuilder(threadName).append("-%d").toString();
        ThreadFactory threadNameVal = new ThreadFactoryBuilder().setNameFormat(threadNameStr).build();
        //线程池
        return new ThreadPoolExecutor(coreSize, maxSize, timeOut, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize), threadNameVal, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 指定参数创建一个线程池
     *
     * @param coreSize
     * @param maxSize
     * @param queueSize
     * @param threadName
     * @param timeOut
     * @return
     */
    public static ThreadPoolExecutor getThreadPoolExecutors(int coreSize, int maxSize, int queueSize, String threadName, long timeOut) {
        return doThreadPoolExecutor(coreSize, maxSize, queueSize, threadName, timeOut);
    }

    /**
     * 创建一个默认线程池
     * 创建默认线程池
     * 核心数：CPU数
     * 最大工作线程 核心数*2
     * 队列大小 Integer最大值
     * <p>
     * 使用IO比较密集的任务(爬虫）
     *
     * @return
     */
    public static ThreadPoolExecutor getDefaultThreadPoolExecutors() {
        return doThreadPoolExecutor(PROCESSORS, PROCESSORS * 2, Integer.MAX_VALUE, "thread", 200L);
    }

    /**
     * 通过线程池得到guava的ListeningExecutorService
     *
     * @param executor
     * @return
     */
    public static ListeningExecutorService getListeningExecutor(ThreadPoolExecutor executor) {
        return MoreExecutors.listeningDecorator(executor);
    }

    /**
     * 使用基本配置创建线程池
     *
     * @param coreSize
     * @param maxSize
     * @param queueSize
     * @return
     */
    public static ListeningExecutorService getGuavaExecutor(int coreSize, int maxSize, int queueSize) {
        return doGuavaExecutor(coreSize, maxSize, queueSize, "thread", 400L);
    }

    /**
     * 所有配置创建线程池，包括线程名和超时时间
     *
     * @param coreSize
     * @param maxSize
     * @param queueSize
     * @param threadName
     * @param timeOut
     * @return
     */
    public static ListeningExecutorService getGuavaExecutor(int coreSize, int maxSize, int queueSize, final String threadName, long timeOut) {
        return doGuavaExecutor(coreSize, maxSize, queueSize, threadName, timeOut);
    }

    /**
     * 创建默认线程池
     * 核心数：CPU数
     * 最大工作线程 核心数*2
     * 队列大小 Integer最大值
     * <p>
     * 使用IO比较密集的任务(爬虫）
     *
     * @return
     */
    public static ListeningExecutorService getDefualtGuavaExecutor() {
        return doGuavaExecutor(PROCESSORS, PROCESSORS * 2, Integer.MAX_VALUE, "Wandering_Earth", 200L);
    }


    public static ListeningExecutorService getGuavaExecutor(int coreSize) {
        return doGuavaExecutor(coreSize, coreSize * 2, Integer.MAX_VALUE, "Wandering_Earth", 200L);
    }

}
