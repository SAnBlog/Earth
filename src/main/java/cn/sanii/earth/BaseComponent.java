package cn.sanii.earth;

import cn.sanii.earth.download.Downloader;
import cn.sanii.earth.event.EventManager;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.enums.EventEnum;
import cn.sanii.earth.pipeline.Pipeline;
import cn.sanii.earth.process.BeforeProcessor;
import cn.sanii.earth.process.Processor;
import cn.sanii.earth.schedule.Scheduler;
import cn.sanii.earth.schedule.SchedulerName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-18 22:35
 * @Description: 公共组件抽象类
 */
public abstract class BaseComponent {

    /**
     * 下载器组件
     */
    protected Downloader downloader;

    /**
     * 管道组件
     */
    protected List<Pipeline> pipelines = Lists.newArrayList();

    /**
     * 处理页面组件
     */
    protected Processor processor;

    /**
     * 任务正式抓取前操作组件
     */
    protected BeforeProcessor beforeProcessor;

    /**
     * 种子列表
     */
    protected List<Request> startRequests = Lists.newArrayList();

    /**
     * cookie
     */
    protected Map<String, String> cookies = Maps.newHashMap();

    /**
     * 调度器组件
     */
    protected Scheduler scheduler;

    /**
     * 线程池
     */
    protected ExecutorService executorService;

    protected Predicate<Request> predicate;

    /**
     * 运行状态
     */
    protected boolean isRun;

    /**
     * 线程数
     */
    protected int threadCount = Runtime.getRuntime().availableProcessors();

    /**
     * 线程空闲等待持续时间
     */
    protected long waitTime;

    /**
     * 允许线程空闲最大等待时间，大于则终止任务。
     */
    protected long allowWaitTime = 30000L;

    public BaseComponent addEvent(Predicate<Request> predicate, Consumer<Request> consumer) {
        this.predicate = predicate;
        EventManager.register(EventEnum.GLOBAL_STARTED, consumer);
        return this;
    }

    public BaseComponent thread(int theadCount) {
        this.threadCount = theadCount;
        return this;
    }

    public BaseComponent setAllowWaitTime(long waitTime) {
        this.allowWaitTime = waitTime;
        return this;
    }

    public BaseComponent addUrl(String... url) {
        for (String s : url) {
            this.startRequests.add(new Request(s, this.processor.name()));
        }
        return this;
    }

    public BaseComponent addUrlAll(List<Request> startRequests) {
        this.startRequests.addAll(startRequests);
        return this;
    }

    public BaseComponent setBeforeProcessor(BeforeProcessor beforeProcessor) {
        this.beforeProcessor = beforeProcessor;
        return this;
    }

    public BaseComponent setProcessor(Processor processor) {
        this.processor = processor;
        return this;
    }

    public BaseComponent setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public BaseComponent setScheduler(Scheduler scheduler) {
        if (scheduler instanceof SchedulerName) {
            ((SchedulerName) scheduler).setFieldName(this.processor.name());
        }
        this.scheduler = scheduler;
        return this;
    }

    public BaseComponent setPipelines(List<Pipeline> pipelines) {
        this.pipelines.addAll(pipelines);
        return this;
    }

    public BaseComponent setPipelines(Pipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }
}
