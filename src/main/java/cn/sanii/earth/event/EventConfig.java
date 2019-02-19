package cn.sanii.earth.event;

import cn.sanii.earth.BaseComponent;
import cn.sanii.earth.event.impl.AsynWanderingListener;
import cn.sanii.earth.process.Processor;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-18 22:35
 * @Description: 异步启动任务配置bean
 */
@Data
@Accessors(chain = true)
public class EventConfig extends BaseComponent {

    private static List<EventListener> listenerList = Lists.newArrayList();

    static {
        listenerList.add(new AsynWanderingListener());
    }

    private EventConfig(Processor processor) {
        this.processor = processor;
    }

    public static EventConfig create(Processor processor) {
        return new EventConfig(processor);
    }

    public static void register(EventListener listener) {
        listenerList.add(listener);
    }

    public static void registerAll(List<EventListener> listenerList) {
        listenerList.forEach(EventConfig::register);
    }

    public static List<EventListener> getEventListener() {
        return listenerList;
    }

}
