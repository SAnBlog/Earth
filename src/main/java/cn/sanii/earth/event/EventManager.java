package cn.sanii.earth.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.enums.EventEnum;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/15 14:34
 * @Description: Consumer管理器
 */
public class EventManager {

    private static final Map<EventEnum, List<Consumer<Request>>> CONSUMERS = Maps.newHashMap();

    /**
     * 注册事件
     * @param eventEnum 事件类型枚举
     * @param consumer consumer表达式
     */
    public static void register(EventEnum eventEnum, Consumer<Request> consumer) {
        List<Consumer<Request>> consumers = CONSUMERS.get(eventEnum);
        if (Objects.isNull(consumers)) {
            consumers = Lists.newArrayList();
        }
        consumers.add(consumer);
        CONSUMERS.put(eventEnum, consumers);
    }

    /**
     * 消费
     * @param eventEnum 事件类型枚举
     * @param request 表达式处理对象
     */
    public static void consumer(EventEnum eventEnum, Request request) {
        Optional.ofNullable(CONSUMERS.get(eventEnum)).ifPresent(consumers -> consumers.forEach(consumer -> consumer.accept(request)));
    }
}
