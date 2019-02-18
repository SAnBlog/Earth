package cn.sanii.earth.example;

import cn.sanii.earth.BaseComponent;
import cn.sanii.earth.event.EventBusCenter;
import cn.sanii.earth.event.EventListener;
import cn.sanii.earth.event.WanderingEvent;
import cn.sanii.earth.pipeline.SaveFilePipeline;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

public class EventTest {


    public static void main(String[] args) throws InterruptedException {

        BaseComponent component = WanderingEvent.create(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .thread(10);


        EventBusCenter.register(new EventListener());
        EventBusCenter.postAsync(component);
    }
}
