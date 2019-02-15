package schedule;

import model.Request;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:09
 * @Description:
 */
public interface Scheduler {

    void push(Request request);

    Request poll();
}
