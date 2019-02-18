package cn.sanii.earth;

import cn.sanii.earth.download.Downloader;
import cn.sanii.earth.event.EventManager;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.enums.EventEnum;
import cn.sanii.earth.pipeline.Pipeline;
import cn.sanii.earth.process.BeforeProcessor;
import cn.sanii.earth.process.Processor;
import cn.sanii.earth.schedule.Scheduler;
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

    protected Downloader downloader;

    protected List<Pipeline> pipelines = Lists.newArrayList();

    protected Processor processor;

    protected BeforeProcessor beforeProcessor;

    protected List<Request> startRequests = Lists.newArrayList();

    protected Map<String, String> cookies = Maps.newHashMap();

    protected Scheduler scheduler;

    protected ExecutorService executorService;

    protected Predicate<Request> predicate;

    protected boolean isRun;

    /**
     * 线程数
     */
    protected int threadCount = Runtime.getRuntime().availableProcessors();

    protected long waitTime;

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
            this.startRequests.add(new Request(s));
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
