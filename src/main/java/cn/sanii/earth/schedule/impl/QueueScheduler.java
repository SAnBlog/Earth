package cn.sanii.earth.schedule.impl;

import cn.sanii.earth.model.Request;
import cn.sanii.earth.schedule.IScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 11:48
 * @Description: 本地队列
 */
public class QueueScheduler implements IScheduler {

    private BlockingQueue<Request> requests = new LinkedBlockingQueue<>();

    @Override
    public void push(Request request) {
        requests.add(request);
    }

    @Override
    public Request poll() {
        return requests.poll();
    }
}
