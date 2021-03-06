package cn.sanii.earth;

import cn.sanii.earth.event.EventBusCenter;
import cn.sanii.earth.event.EventConfig;
import cn.sanii.earth.example.MzituProcessor;
import cn.sanii.earth.example.PengfueProcessor;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.IProcessor;
import cn.sanii.earth.schedule.impl.RedisScheduler;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 16:23
 * @Description: 地球
 */
public class Earth {

    private Earth() {
    }


    /**
     * 同步执行
     *
     * @param processor
     * @return
     */
    public static Wandering me(IProcessor processor) {
        return new Wandering(processor);
    }

    /**
     * 异步执行
     *
     * @param component
     * @return
     */
    public static void asyn(BaseComponent component) {
        EventConfig.getEventListener().forEach(EventBusCenter::register);
        EventBusCenter.postAsync(component);
    }


    public static void main(String[] args) {

        //异步 妹子图下载
        BaseComponent component = EventConfig.create(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request));

        Earth.asyn(component);
    }

    @Test
    public void testSyn() {
        //同步 本地内存队列
        Earth.me(new PengfueProcessor())
                .addUrl("https://www.pengfue.com/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .start();
    }

    @Test
    public void testASyn() {
        //异步 Redis队列
        BaseComponent component = EventConfig.create(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .setScheduler(new RedisScheduler("127.0.0.1"));

        Earth.asyn(component);
    }


}
