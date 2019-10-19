package cn.sanii.earth;

import cn.sanii.earth.download.IDownloader;
import cn.sanii.earth.download.impl.HttpDownloader;
import cn.sanii.earth.event.EventManager;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.EventEnum;
import cn.sanii.earth.pipeline.IPipeline;
import cn.sanii.earth.pipeline.impl.ConsolePipeline;
import cn.sanii.earth.process.BaseBeforeProcessor;
import cn.sanii.earth.process.IAfterProcessor;
import cn.sanii.earth.process.IProcessor;
import cn.sanii.earth.process.impl.ConsoleAfterProcessor;
import cn.sanii.earth.schedule.IScheduler;
import cn.sanii.earth.schedule.impl.QueueScheduler;
import cn.sanii.earth.util.GuavaThreadPoolUtils;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 16:46
 * @Description: 流浪
 */
@Slf4j
public class Wandering extends BaseComponent {

    public Wandering(IProcessor processor) {
        this.processor = processor;
    }

    public Wandering(BaseComponent component) {
        this.processor = component.processor;
        this.downloader = component.downloader;
        this.pipelines.addAll(component.pipelines);
        this.processor = component.processor;
        this.beforeProcessor = component.beforeProcessor;
        this.afterProcessor = component.afterProcessor;
        this.startRequests.addAll(component.startRequests);
        this.cookies.putAll(component.cookies);
        this.scheduler = component.scheduler;
        this.executorService = component.executorService;
        this.predicate = component.predicate;
        this.isRun = component.isRun;
        this.threadCount = component.threadCount;
        this.waitTime = component.waitTime;
        this.allowWaitTime = component.allowWaitTime;
    }


    public void start() {
        if (this.isRun) {
            log.error("Wandering is runing");
            throw new RuntimeException("Wandering is runing");
        }

        log.warn("北京第三区交通委提醒你:道路千万条,安全第一条,行车不规范,亲人两行泪");

        initComponent();
        this.isRun = true;

        while (this.isRun) {
            Stopwatch started = Stopwatch.createStarted();
            final Request request = scheduler.poll();

            if (Objects.nonNull(request)) {
                if (predicate.test(request)) {
                    EventManager.consumer(EventEnum.GLOBAL_STARTED, request);
                }
                executorService.submit(() -> handler(request));
                waitTime = 0;
            }

            trySleep();

            checkTask(started, request);
        }
    }

    private void checkTask(Stopwatch started, Request request) {
        if (Objects.isNull(request)) {
            waitTime += started.elapsed(TimeUnit.MILLISECONDS);
        }

        if (waitTime > allowWaitTime) {
            stop();
            Optional.ofNullable(this.afterProcessor).ifPresent(IAfterProcessor::process);
        }
    }

    public void stop() {
        if (this.isRun) {
            this.isRun = false;
        }
    }

    private void trySleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("trySleep", e);
        }
    }

    private void handler(Request request) {
        request.setCookies(this.cookies).setName(this.processor.name());
        Response response = downloader.download(request);
        response.setName(this.processor.name());
        log.info("response:{}", response);
        if (!(response.isSuccess() && successHandler(response))) {
            log.info("fail!!!:{}",request.getUrl());
        }
    }


    private void failHandler(Request request) {
        scheduler.push(request);
    }

    private boolean successHandler(Response response) {
        try {
            processor.process(response);

            pipelines.forEach(pipeline -> pipeline.process(response));

            response.getResultField().getRequests().forEach(scheduler::push);

            statisticsTask(response);
        } catch (Exception e) {
            log.error("successHandler fail",e);
            return false;
        }
        return true;
    }

    private void statisticsTask(Response response) {
        statistics.addAndGet(response.getResultField().getRequests().size());

        if (CollectionUtils.isEmpty(response.getResultField().getRequests())) {
            log.info("任务终止，完成任务数:{}", statistics.get());
        }
    }


    /**
     * init
     */
    private synchronized void initComponent() {
        log.info("init Component ...");
        if (Objects.isNull(downloader)) {
            downloader = new HttpDownloader();
        }
        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }
        if (Objects.isNull(scheduler)) {
            scheduler = new QueueScheduler();
        }

        this.startRequests.stream().map(request -> request.setName(this.processor.name())).forEach((this.scheduler::push));

        if (Objects.isNull(executorService)) {
            executorService = GuavaThreadPoolUtils.getGuavaExecutor(this.threadCount);
        }
        if (Objects.nonNull(beforeProcessor)) {
            this.cookies.putAll(beforeProcessor.init());
        }
        if (Objects.nonNull(afterProcessor)) {
            afterProcessor = new ConsoleAfterProcessor();
        }
        log.info("init success");
    }

    @Override
    public Wandering addEvent(Predicate<Request> predicate, Consumer<Request> consumer) {
        super.addEvent(predicate, consumer);
        return this;
    }

    @Override
    public Wandering thread(int theadCount) {
        super.thread(theadCount);
        return this;
    }

    @Override
    public Wandering setAllowWaitTime(long waitTime) {
        super.setAllowWaitTime(waitTime);
        return this;
    }

    @Override
    public Wandering addUrl(Charset charset, String... url) {
        super.addUrl(charset, url);
        return this;
    }

    @Override
    public Wandering addUrl(String... url) {
        super.addUrl(url);
        return this;
    }

    @Override
    public Wandering addUrlAll(List<Request> startRequests) {
        super.addUrlAll(startRequests);
        return this;
    }

    @Override
    public Wandering setBeforeProcessor(BaseBeforeProcessor beforeProcessor) {
        super.setBeforeProcessor(beforeProcessor);
        return this;
    }

    @Override
    public Wandering setProcessor(IProcessor processor) {
        super.setProcessor(processor);
        return this;
    }

    @Override
    public Wandering setDownloader(IDownloader downloader) {
        super.setDownloader(downloader);
        return this;
    }

    @Override
    public Wandering setScheduler(IScheduler scheduler) {
        super.setScheduler(scheduler);
        return this;
    }

    @Override
    public Wandering setPipelines(List<IPipeline> pipelines) {
        super.setPipelines(pipelines);
        return this;
    }

    @Override
    public Wandering setPipelines(IPipeline pipeline) {
        super.setPipelines(pipeline);
        return this;
    }
}
