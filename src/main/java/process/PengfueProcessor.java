package process;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import model.Request;
import model.Response;
import model.ResultField;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

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

        List<String> titles = Lists.newArrayList();
        List<String> contents = Lists.newArrayList();
        document.getElementsByClass("list-item").forEach(element -> {
            String title = element.getElementsByClass("dp-b").first().getElementsByTag("a").html();
            String val = element.getElementsByClass("content-img").first().html();
            titles.add(title);
            contents.add(val);
        });

        resultField.getFields().put("title", titles);
        resultField.getFields().put("contents", contents);

    }

    @Override
    public String name() {
        return "Pengfue";
    }

}
