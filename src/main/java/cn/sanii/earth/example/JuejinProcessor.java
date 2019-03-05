package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.download.impl.PhantomJSDownloader;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.IProcessor;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: Administrator
 * @create: 2019/3/5
 * Description: https://juejin.im/
 */
@Slf4j
public class JuejinProcessor implements IProcessor {

    private final static String PRIFIX = "https://juejin.im";

    @Override
    public void process(Response response) {
        Document document = response.getDocument();
        //扫描主页热门推荐
        Elements item = document.getElementsByClass("item");
        item.forEach(element -> {
            Element elements = element.getElementsByAttributeValue("class", "title").first();
            if (Objects.nonNull(elements)) {
                String text = elements.text();
                Optional<Element> optional = Optional.ofNullable(elements.getElementsByAttribute("href").first());
                optional.ifPresent(element1 -> {
                    String href = PRIFIX.concat(element1.attr("href"));
                    response.getResultField().getRequests().add(new Request(href, name()));
                    log.info("Result：{},{}", text, href);
                });
            }
        });

        //具体文章内容
        HashMap<String, String> map = Maps.newHashMap();
        Element title = document.getElementsByAttributeValue("class", "article-title").first();
        if (Objects.nonNull(title)) {
            String text = document.getElementsByAttributeValue("class", "article-content").first().text();
            log.info("text:{}", text);
            map.put(title.text(),text);
        }
        response.getResultField().getFields().put(FieldEnum.TEXT, map);
    }

    @Override
    public String name() {
        return "juejin";
    }

    public static void main(String[] args) {
        Earth.me(new JuejinProcessor())
                .addUrl("https://juejin.im/")
                .setPipelines(new SaveFilePipeline())
                .setDownloader(new PhantomJSDownloader())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .start();
    }
}
