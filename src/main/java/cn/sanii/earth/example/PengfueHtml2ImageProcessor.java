package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.download.impl.HttpDownloader;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.ResultField;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.IProcessor;
import cn.sanii.earth.util.Html2Image.CssboxUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 11:21
 * @Description: https://www.pengfue.com/
 */
@Slf4j
public class PengfueHtml2ImageProcessor implements IProcessor {

    @Override
    public void process(Response response) {
        final ResultField resultField = response.getResultField();

        Document document = response.getDocument();
        Elements nextPages = document.getElementsByClass("page").first().getElementsByClass("on");
        nextPages.forEach(element -> {
            if (element.text().contains("下一页")) {
                String attr = element.attr("href");
                log.info("next page:{}", attr);
                resultField.getRequests().add(new Request(attr, name()));
            }
        });

        try {
            String url = response.getRequest().getUrl();
            byte[] bytes = CssboxUtil.toByte(url);
            HashMap<String, byte[]> map = Maps.newHashMap();
            map.put(System.currentTimeMillis() + "", bytes);
            resultField.getFields().put(FieldEnum.BYTES, map);
        } catch (Exception e) {
            log.error("CssboxUtil process", e);
        }

    }

    @Override
    public String name() {
        return "Pengfue";
    }

    public static void main(String[] args) {
        Earth.me(new PengfueHtml2ImageProcessor())
                .addUrl("https://www.pengfue.com/")
                .setPipelines(new SaveFilePipeline())
                .setDownloader(new HttpDownloader())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .start();
    }
}
