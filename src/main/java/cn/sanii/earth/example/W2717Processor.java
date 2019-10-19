package cn.sanii.earth.example;

import cn.sanii.earth.Earth;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.pipeline.impl.SaveFilePipeline;
import cn.sanii.earth.process.BaseImgProcessor;
import org.jsoup.nodes.Document;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author: Administrator
 * @create: 2019/2/21
 * Description: https://www.2717.com/tag/434.html
 */
public class W2717Processor extends BaseImgProcessor {

    private final static String SITE = "https://www.2717.com";

    @Override
    public void process(Response response) {

        Document document = response.getDocument();
        document.getElementsByClass("TagPage").first().getElementsByTag("li").forEach(element -> {
            if (Objects.nonNull(element) && element.text().contains("下一页")) {
                String nextPage = SITE.concat(element.getElementsByTag("a").first().attr("href"));
                response.getResultField().getRequests().add(new Request(nextPage, name(),response.getRequest().getCharset()));
            }
        });
        super.process(response);
    }

    @Override
    public String name() {
        return "W2717";
    }

    public static void main(String[] args) {
        Earth.me(new W2717Processor())
                .addUrl(Charset.forName("GB2312"),"https://www.2717.com/tag/434.html")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + request))
                .start();

    }
}
