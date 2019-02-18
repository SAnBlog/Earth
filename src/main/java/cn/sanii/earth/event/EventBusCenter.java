package cn.sanii.earth.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-18 22:35
 * @Description: 管理所有异步/同步事件总线
 */
public class EventBusCenter {


    /**
     * 管理同步事件
     */
    public static EventBus syncEventBus = new EventBus();

    /**
     * 管理异步事件
     */
    public static AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool());


    /**
     * 发送一个同步消息
     *
     * @param o
     */
    public static void postSync(Object o) {
        syncEventBus.post(o);
    }

    /**
     * 发送一个异步消息
     *
     * @param o
     */
    public static void postAsync(Object o) {
        asyncEventBus.post(o);
    }

    /**
     * 注册为监听者
     *
     * @param o
     */
    public static void register(Object o) {
        syncEventBus.register(o);
        asyncEventBus.register(o);
    }

}