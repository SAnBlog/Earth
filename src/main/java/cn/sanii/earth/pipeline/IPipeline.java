package cn.sanii.earth.pipeline;

import cn.sanii.earth.model.Response;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:07
 * @Description: 处理管道接口
 */
public interface IPipeline {

    /**
     * 默认路径
     */
    String FILE_PATH = "D:/";

    /**
     * 处理页面响应
     * @param response
     */
    void process(Response response);

}
