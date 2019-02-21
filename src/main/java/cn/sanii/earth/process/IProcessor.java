package cn.sanii.earth.process;

import cn.sanii.earth.model.Response;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:08
 * @Description: 页面处理接口
 */
public interface IProcessor {

    /**
     * 对下载的页面进行处理
     * @param response
     */
    void process(Response response);

    /**
     *  任务name
     * @return
     */
    String name();
}
