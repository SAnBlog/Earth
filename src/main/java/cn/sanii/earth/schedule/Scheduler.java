package cn.sanii.earth.schedule;

import cn.sanii.earth.model.Request;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:09
 * @Description: 调度接口
 */
public interface Scheduler{

    /**
     * 推入一个任务到队列
     * @param request
     */
    void push(Request request);

    /**
     * 从队列中弹出一个任务
     * @return
     */
    Request poll();
}
