package model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:01
 * @Description:
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private Request request;

    private ResultField resultField = new ResultField();

    private boolean success;

    private String name;

    private String html;

    private Document document;

    public void setResult(String html) {
        Document parse = Jsoup.parse(html);
        this.html = html;
        this.document = parse;
    }
}
