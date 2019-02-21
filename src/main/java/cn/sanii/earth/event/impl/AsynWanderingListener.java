package cn.sanii.earth.event.impl;

import cn.sanii.earth.BaseComponent;
import cn.sanii.earth.Wandering;
import cn.sanii.earth.event.IEventListener;
import cn.sanii.earth.util.GuavaThreadPoolUtils;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/19 10:09
 * @Description: 异步监听任务
 */
@Slf4j
public class AsynWanderingListener implements IEventListener<BaseComponent> {

    /**
     * 线程池，控制消费线程数
     */
    private static final ListeningExecutorService executor = GuavaThreadPoolUtils.getDefualtGuavaExecutor();

    @Subscribe
    @Override
    public void message(BaseComponent event) {
        executor.submit(() -> new Wandering(event).start());
    }
}
