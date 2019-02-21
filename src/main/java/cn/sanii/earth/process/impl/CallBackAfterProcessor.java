package cn.sanii.earth.process.impl;

import cn.sanii.earth.process.IAfterProcessor;
import cn.sanii.earth.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: Administrator
 * @create: 2019/2/21
 * Description: 执行回调URL
 */
@Slf4j
public class CallBackAfterProcessor implements IAfterProcessor {

    private static OkHttpClient client;

    /**
     * 被回调URL
     */
    private String url;

    public CallBackAfterProcessor(String url) {
        this.url = url;
        if (Objects.isNull(client)) {
            client = OkHttpUtil.getDefault();
        }
    }

    @Override
    public void process() {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            boolean successful = client.newCall(request).execute().isSuccessful();
            log.info("callback result:{}", successful);
        } catch (IOException e) {
            log.error("CallBackAfterProcessor process error", e);
        }
    }
}
