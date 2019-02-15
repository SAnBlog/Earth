package pipeline;

import model.Response;
import model.ResultField;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:07
 * @Description:
 */
public interface Pipeline {

    void process(Response response);

}
