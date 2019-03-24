package cn.sanii.earth.download.impl;

import cn.sanii.earth.download.IDownloader;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.util.OkHttpUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 10:54
 * @Description: http下载器
 */
@Slf4j
public class HttpDownloader implements IDownloader {

    private static OkHttpClient ok;

    @Override
    public Response download(Request request) {
        Response response = new Response();
        response.setSuccess(false).setRequest(request);
        if (Objects.isNull(ok)) {
            ok = OkHttpUtil.getDefault();
        }
        try {
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                    .url(request.getUrl())
                    .get();
            if (!request.getCookies().isEmpty()) {
                String cookie = Joiner.on(";").withKeyValueSeparator("=").join(request.getCookies());
                request.getHeaders().put("Cookie", cookie);
            }
            request.getHeaders().forEach(builder::addHeader);

            okhttp3.Request res = builder.build();

            byte[] b = ok.newCall(res).execute().body().bytes();
            String result = new String(b, request.getCharset());
            response.setResult(result);
            response.setSuccess(true);
        } catch (IOException e) {
            log.error("http call error", e);
        }
        return response;
    }

}
