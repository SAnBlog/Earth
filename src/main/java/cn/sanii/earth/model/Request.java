package cn.sanii.earth.model;

import cn.sanii.earth.download.UserAgent;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 17:00
 * @Description:
 */
@Data
public class Request {

    private String url;

    /**
     * cookies for current url, if not set use Site's cookies
     */
    private Map<String, String> cookies = Maps.newHashMap();

    private Map<String, String> headers = Maps.newHashMap();

    public Request(String url) {
        this.url = url;
        this.headers.put("User-Agent", UserAgent.CHROME_FOR_MAC);
    }
}
