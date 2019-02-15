package common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import model.Request;
import model.enums.EventEnum;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/15 14:34
 * @Description:
 */
public class EventManager {

    private static final Map<EventEnum, List<Consumer<Request>>> CONSUMERS = Maps.newHashMap();

    public static void register(EventEnum eventEnum, Consumer<Request> consumer) {
        List<Consumer<Request>> consumers = CONSUMERS.get(eventEnum);
        if (Objects.isNull(consumers)) {
            consumers = Lists.newArrayList();
        }
        consumers.add(consumer);
        CONSUMERS.put(eventEnum, consumers);
    }

    public static void consumer(EventEnum eventEnum, Request request) {
        Optional.ofNullable(CONSUMERS.get(eventEnum)).ifPresent(consumers -> consumers.forEach(consumer -> consumer.accept(request)));
    }
}
