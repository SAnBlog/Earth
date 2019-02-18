package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.SaveFilePipeline;
import cn.sanii.earth.process.Processor;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019年2月15日 21:01:59
 * @Description: https://www.mzitu.com/zipai/
 */
public class MzituProcessor implements Processor {

    @Override
    public void process(Response response) {
        Document document = response.getDocument();

        //获取下一个种子地址
        document.getElementsByClass("pagenavi-cm").first().getElementsByTag("a").forEach(a -> {
            if (Objects.nonNull(a) && a.text().contains("下一页")) {
                String nextPage = a.attr("href");
                response.getResultField().getRequests().add(new Request(nextPage));
            }
        });

        /**
         * 图片地址提取规则
         */
        HashMap<String, String> images = Maps.newHashMap();
        document.getElementsByClass("lazy").forEach(element -> {
            String img = element.attr("data-original");
            images.put(System.currentTimeMillis() + "", img);
        });

        response.getResultField().getFields().put(FieldEnum.BYTE, images);
    }

    @Override
    public String name() {
        return "Mzitu";
    }

    public static void main(String[] args) {
        Earth.me(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .thread(10)
                .start();
    }
}
