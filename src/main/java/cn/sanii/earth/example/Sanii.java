package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.Wandering;
import cn.sanii.earth.pipeline.SaveFilePipeline;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/15 18:29
 * @Description:
 */
public class Sanii {
    public static void main(String[] args) {
        final String PAGE_URL = "https://sanii.cn/article/%s";

        String url = "https://sanii.cn/article/281";
        Integer page = Integer.valueOf(url.substring(url.lastIndexOf("/") + 1));

        Wandering wandering = Earth.me(new SaniiProcessor())
                .addUrl("https://sanii.cn/article/281")
                .setPipelines(new SaveFilePipeline());

        for (int i = page; i > 0; i--) {
            wandering.addUrl(String.format(PAGE_URL, i));
        }

        wandering
                .setBeforeProcessor(new SaniiBeforeProcessor())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .start();
    }
}
