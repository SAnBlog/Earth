package cn.sanii.earth.event;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-18 22:35
 * @Description: 事件驱动监听者
 */
public interface IEventListener<T> {

    /**
     * 消费事件
     * @param t
     */
    void message(T t);
}
