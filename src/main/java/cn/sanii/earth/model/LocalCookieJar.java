package cn.sanii.earth.model;

import com.google.common.collect.Lists;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: shouliang.wang
 * @Date: 2018/10/10 11:50
 * @Description: CookieJar是用于保存Cookie的
 */
public class LocalCookieJar implements CookieJar {
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieStore.get(httpUrl.host());
        return cookies != null ? cookies : new ArrayList<>();
    }

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        List<Cookie> cookieList = cookieStore.get(httpUrl.host());
        List<Cookie> newlist = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(cookieList)) {
            cookieList.forEach(newlist::add);
        }

        if (CollectionUtils.isNotEmpty(cookies)) {
            cookies.forEach(newlist::add);
        }
        cookieStore.put(httpUrl.host(), newlist);
    }
}