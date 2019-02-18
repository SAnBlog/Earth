package cn.sanii.earth.event;

import cn.sanii.earth.BaseComponent;
import cn.sanii.earth.Wandering;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-18 22:35
 * @Description: 事件驱动监听者
 */
@Slf4j
public class EventListener {

    @Subscribe
    public void message(BaseComponent event) {
        log.info("收到消息:{}", JSONObject.toJSONString(event));
    }
}