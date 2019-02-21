package cn.sanii.earth.event;

import cn.sanii.earth.BaseComponent;
import cn.sanii.earth.event.impl.AsynWanderingListener;
import cn.sanii.earth.process.IProcessor;
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

    /**
     * 事件监听器
     */
    private static List<IEventListener> listenerList = Lists.newArrayList();

    static {
        listenerList.add(new AsynWanderingListener());
    }

    private EventConfig(IProcessor processor) {
        this.processor = processor;
    }

    public static EventConfig create(IProcessor processor) {
        return new EventConfig(processor);
    }

    public static void register(IEventListener listener) {
        listenerList.add(listener);
    }

    public static void registerAll(List<IEventListener> listenerList) {
        listenerList.forEach(EventConfig::register);
    }

    public static List<IEventListener> getEventListener() {
        return listenerList;
    }

}
