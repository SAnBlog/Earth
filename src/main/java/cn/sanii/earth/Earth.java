package cn.sanii.earth;

import com.alibaba.fastjson.JSONObject;
import cn.sanii.earth.pipeline.SaveFilePipeline;
import cn.sanii.earth.example.PengfueProcessor;
import cn.sanii.earth.process.Processor;

import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 16:23
 * @Description: 地球
 */
public class Earth {

    private Earth() {
    }

    public static Wandering me(Processor processor) {
        return new Wandering(processor);
    }

    public static void main(String[] args) {
        Earth.me(new PengfueProcessor())
                .addUrl("https://www.pengfue.com/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .start();
    }
}
