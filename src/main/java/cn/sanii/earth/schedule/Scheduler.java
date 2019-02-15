package cn.sanii.earth.schedule;

import cn.sanii.earth.model.Request;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:09
 * @Description:
 */
public interface Scheduler {

    void push(Request request);

    Request poll();
}
