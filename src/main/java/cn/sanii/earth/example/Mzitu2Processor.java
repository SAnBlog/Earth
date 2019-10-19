package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.IProcessor;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author: shouliang.wang
 * @Date: 2019-02-21 21:09:25
 * @Description: https://www.mzitu.com/xinggan/
 */
public class Mzitu2Processor implements IProcessor {

    @Override
    public void process(Response response) {
        Document document = response.getDocument();

        //获取下一个种子地址
        document.getElementsByClass("nav-links").first().getElementsByTag("a").forEach(a -> {
            if (Objects.nonNull(a) && a.text().contains("下一页")) {
                String nextPage = a.attr("href");
                response.getResultField().getRequests().add(new Request(nextPage, name()));
            }
        });

        /**
         * 图片地址提取规则
         */
        HashMap<String, String> images = Maps.newHashMap();
        document.getElementsByTag("img").forEach(element -> {
            String img = element.attr("src");
            images.put(UUID.randomUUID().toString(), img);
        });


        response.getResultField().getFields().put(FieldEnum.BYTE, images);
    }

    @Override
    public String name() {
        return "Mzitu";
    }

    public static void main(String[] args) {
        Earth.me(new Mzitu2Processor())
                .addUrl("https://www.mzitu.com/xinggan/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" +request))
                .start();
    }
}
