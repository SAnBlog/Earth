package cn.sanii.earth.model;

import cn.sanii.earth.download.UserAgent;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:00
 * @Description: 抓取请求
 */
@Data
@Accessors(chain = true)
public class Request {

    private String url;

    private String name;

    /**
     * 页面编码格式
     */
    private Charset charset;

    /**
     * cookies for current url, if not set use Site's cookies
     */
    private Map<String, String> cookies = Maps.newHashMap();

    private Map<String, String> headers = Maps.newHashMap();

    /**
     * 实现了SchedulerName接口请使用该方法
     *
     * @param url
     * @param name 任务id
     * @See cn.sanii.earth.schedule.SchedulerName
     */
    public Request(String url, String name) {
        this(url, name, Charset.defaultCharset());
    }

    /**
     *
     * @param url
     * @param name
     * @param charset 页面编码格式
     */
    public Request(String url, String name, Charset charset) {
        this.url = url;
        this.name = name;
        this.charset = charset;
        this.headers.put("User-Agent", UserAgent.CHROME_FOR_MAC);
        this.headers.put("Referer", url);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
