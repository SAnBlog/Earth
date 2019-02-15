package cn.sanii.earth.process;

import cn.sanii.earth.model.Response;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:08
 * @Description:
 */
public interface Processor {

    void process(Response response);

    String name();
}
