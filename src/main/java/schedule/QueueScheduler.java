package schedule;

import model.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 11:48
 * @Description: 本地队列
 */
public class QueueScheduler implements Scheduler {

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
