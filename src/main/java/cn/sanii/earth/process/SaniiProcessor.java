package cn.sanii.earth.process;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.ResultField;
import cn.sanii.earth.model.enums.FieldEnum;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 14:55
 * @Description:
 */
@Slf4j
public class SaniiProcessor implements Processor {

    @Override
    public void process(Response response) {
//        https://sanii.cn/article/281

        ResultField resultField = response.getResultField();
//        if (response.getHtml().contains("找不到")){
//            resultField.setSkip(true);
//            return;
//        }

        Document document = response.getDocument();
        List<String> titles = Lists.newArrayList();
        String title = document.getElementsByClass("post-title").first().getElementsByTag("a").first().text();
        titles.add(title);
        resultField.getFields().put(FieldEnum.TEXT, titles);
    }

    @Override
    public String name() {
        return "sanii";
    }

}
