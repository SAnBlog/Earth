package cn.sanii.earth.process;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.ResultField;
import cn.sanii.earth.model.enums.FieldEnum;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.UUID;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 11:21
 * @Description: https://www.pengfue.com/
 */
@Slf4j
public class PengfueProcessor implements Processor {

    @Override
    public void process(Response response) {
        final ResultField resultField = response.getResultField();

        Document document = response.getDocument();
        Elements nextPages = document.getElementsByClass("page").first().getElementsByClass("on");
        nextPages.forEach(element -> {
            if (element.text().contains("下一页")) {
                String attr = element.attr("href");
                log.info("next page:{}", attr);
                resultField.getRequests().add(new Request(attr));
            }
        });

        Map<String, String> result = Maps.newHashMap();
        Map<String, String> images = Maps.newHashMap();
        document.getElementsByClass("list-item").forEach(element -> {
            //image
            String image = element.getElementsByClass("mem-header").first().getElementsByTag("img").attr("src");
            images.put(UUID.randomUUID().toString(), image);
            String title = element.getElementsByClass("dp-b").first().getElementsByTag("a").html();
            String val = element.getElementsByClass("content-img").first().html();
            result.put(title, val);
        });

        resultField.getFields().put(FieldEnum.TEXT, result);
        resultField.getFields().put(FieldEnum.BYTE, images);

    }

    @Override
    public String name() {
        return "Pengfue";
    }

}
