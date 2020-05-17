package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.IProcessor;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;

/**
 * 微博热搜榜
 */
public class WeiboProcessor implements IProcessor {

    private static final String PREFIX = "https://s.weibo.com";
    @Override
    public void process(Response response) {
        Document document = response.getDocument();

        HashMap<String, String> map = Maps.newLinkedHashMap();
        //获取下一个种子地址
        document.getElementsByTag("tr").forEach(e -> {
            try {
                Element href = e.getElementsByClass("td-02").first().getElementsByTag("a").first();
                String content = href.text();
                String url = href.attr("href");
                if (!StringUtils.isBlank(content)){
                    map.put(content,PREFIX+url);
                }
            } catch (Exception e1) {
            }

        });

        response.getResultField().getFields().put(FieldEnum.TEXT, map);
    }

    @Override
    public String name() {
        return "weibo";
    }

    public static void main(String[] args) {
        List<Response> responses = Earth.me(new WeiboProcessor())
                .addUrl("https://s.weibo.com/top/summary?display=0&retcode=6102")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .run();
        responses.forEach(res -> {
                System.out.println(JSONObject.toJSONString(res.getResultField().getFields().get(FieldEnum.TEXT)));
            ;
        });
    }
}
