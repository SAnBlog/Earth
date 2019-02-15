package cn.sanii.earth.pipeline;

import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.ResultField;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:07
 * @Description:
 */
public interface Pipeline {

    /**
     * 默认路径
     */
    String FILE_PATH = "D:/";

    void process(Response response);

}
