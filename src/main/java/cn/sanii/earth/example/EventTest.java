package cn.sanii.earth.example;

import cn.sanii.earth.BaseComponent;
import cn.sanii.earth.event.EventBusCenter;
import cn.sanii.earth.event.EventConfig;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

public class EventTest {


    public static void main(String[] args) throws InterruptedException {

        BaseComponent component = EventConfig.create(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .thread(10);


        EventBusCenter.postAsync(component);
    }
}
