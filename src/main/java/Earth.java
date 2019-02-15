import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import pipeline.SaveFilePipeline;
import process.PengfueProcessor;
import process.Processor;
import process.SaniiBeforeProcessor;
import process.SaniiProcessor;

import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 16:23
 * @Description: 地球
 */
public class Earth {

    private Earth() {
    }

    public static Wandering me(Processor processor) {
        return new Wandering(processor);
    }

    public static void main(String[] args) {

        Earth.me(new PengfueProcessor())
                .addUrl("https://www.pengfue.com/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .start();
    }

    @Test
    public void sanii() {
        final String PAGE_URL = "https://sanii.cn/article/%s";

        String url = "https://sanii.cn/article/281";
        Integer page = Integer.valueOf(url.substring(url.lastIndexOf("/") + 1));

        Wandering wandering = Earth.me(new SaniiProcessor())
                .addUrl("https://sanii.cn/article/281")
                .setPipelines(new SaveFilePipeline());

        for (int i = page; i > 0; i--) {
            wandering.addUrl(String.format(PAGE_URL, i));
        }

        wandering
                .setBeforeProcessor(new SaniiBeforeProcessor())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .start();
    }
}
