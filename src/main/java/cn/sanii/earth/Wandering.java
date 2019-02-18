package cn.sanii.earth;

import cn.sanii.earth.download.Downloader;
import cn.sanii.earth.download.HttpDownloader;
import cn.sanii.earth.event.EventManager;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.EventEnum;
import cn.sanii.earth.pipeline.ConsolePipeline;
import cn.sanii.earth.pipeline.Pipeline;
import cn.sanii.earth.process.BeforeProcessor;
import cn.sanii.earth.process.Processor;
import cn.sanii.earth.schedule.QueueScheduler;
import cn.sanii.earth.schedule.Scheduler;
import cn.sanii.earth.util.GuavaThreadPoolUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
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

    public Wandering(Processor processor) {
        this.processor = processor;
    }


    public void start() {
        if (this.isRun) {
            log.error("Wandering is runing");
            throw new RuntimeException("Wandering is runing");
        }

        log.warn("北京第三区交通委提醒你:道路千万条,安全第一条,行车不规范,亲人两行泪");

        inintComponent();
        this.isRun = true;

        while (this.isRun) {
            Stopwatch started = Stopwatch.createStarted();
            final Request request = scheduler.poll();

            if (predicate.test(request)) {
                EventManager.consumer(EventEnum.GLOBAL_STARTED, request);
            }

            if (Objects.nonNull(request)) {
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
        }
    }

    public void stop() {
        if (this.isRun != false) {
            this.isRun = false;
        }
        log.info("stop...");
    }

    private void trySleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("trySleep", e);
        }
    }

    private void handler(Request request) {
        request.setCookies(this.cookies);
        Response response = downloader.download(request);
        response.setName(this.processor.name());
        log.info("response:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            log.info("success");
            success(response);
        } else {
            log.info("fail");
            fail(request);
        }
    }


    private void fail(Request request) {
        scheduler.push(request);
    }

    private void success(Response response) {
        processor.process(response);

        pipelines.forEach(pipeline -> pipeline.process(response));

        response.getResultField().getRequests().forEach(scheduler::push);
    }


    private void consumerEvent(Request request, Predicate<Request> predicate) {
        if (predicate.test(request)) {
            EventManager.consumer(EventEnum.GLOBAL_STARTED, request);
        }
    }

    /**
     * init
     */
    private synchronized void inintComponent() {
        log.info("inint Component ...");
        if (Objects.isNull(downloader)) {
            downloader = new HttpDownloader();
        }
        if (pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }
        if (Objects.isNull(scheduler)) {
            scheduler = new QueueScheduler();
            this.startRequests.forEach(this.scheduler::push);
        }
        if (Objects.isNull(executorService)) {
            executorService = GuavaThreadPoolUtils.getGuavaExecutor(this.threadCount);
        }
        if (Objects.nonNull(beforeProcessor)) {
            this.cookies.putAll(beforeProcessor.init());
        }
        log.info("init success");
    }


    @Override
    public Wandering addEvent(Predicate<Request> predicate, Consumer<Request> consumer) {
        this.predicate = predicate;
        EventManager.register(EventEnum.GLOBAL_STARTED, consumer);
        return this;
    }

    @Override
    public Wandering thread(int theadCount) {
        this.threadCount = theadCount;
        return this;
    }

    @Override
    public Wandering setAllowWaitTime(long waitTime) {
        this.allowWaitTime = waitTime;
        return this;
    }

    @Override
    public Wandering addUrl(String... url) {
        for (String s : url) {
            this.startRequests.add(new Request(s));
        }
        return this;
    }

    @Override
    public Wandering addUrlAll(List<Request> startRequests) {
        this.startRequests.addAll(startRequests);
        return this;
    }

    @Override
    public Wandering setBeforeProcessor(BeforeProcessor beforeProcessor) {
        this.beforeProcessor = beforeProcessor;
        return this;
    }

    @Override
    public Wandering setProcessor(Processor processor) {
        this.processor = processor;
        return this;
    }

    @Override
    public Wandering setDownloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    @Override
    public Wandering setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    @Override
    public Wandering setPipelines(List<Pipeline> pipelines) {
        this.pipelines.addAll(pipelines);
        return this;
    }

    @Override
    public Wandering setPipelines(Pipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }
}
