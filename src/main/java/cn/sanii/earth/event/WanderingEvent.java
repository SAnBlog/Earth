package cn.sanii.earth.event;

import cn.sanii.earth.BaseComponent;
import cn.sanii.earth.process.Processor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-18 22:35
 * @Description: 异步启动任务配置bean
 */
@Data
@Accessors(chain = true)
@Builder
public class WanderingEvent extends BaseComponent {

    private WanderingEvent(Processor processor) {
        this.processor = processor;
    }

    public static WanderingEvent create(Processor processor) {
        return new WanderingEvent(processor);
    }
}
