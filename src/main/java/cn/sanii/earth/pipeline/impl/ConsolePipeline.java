package cn.sanii.earth.pipeline.impl;

import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.IPipeline;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 11:19
 * @Description: 默认 打印到控制台
 */
@Slf4j
public class ConsolePipeline implements IPipeline {
    @Override
    public void process(Response response) {
        Map<FieldEnum, Object> fields = response.getResultField().getFields();
        fields.forEach((key, val) -> log.info("ConsolePipeline key:{},value:{}", key, val));
    }
}
