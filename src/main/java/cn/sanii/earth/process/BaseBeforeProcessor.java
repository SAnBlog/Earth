package cn.sanii.earth.process;

import com.google.common.collect.Maps;
import okhttp3.OkHttpClient;
import cn.sanii.earth.util.OkHttpUtil;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 19:05
 * @Description: 抓取前处理接口，可以获取一些参数并覆盖调度系统对象
 */
public abstract class BaseBeforeProcessor {

    protected OkHttpClient ok;

    protected Map<String, String> cookies = Maps.newHashMap();

    public Map<String, String> init() {
        if (Objects.isNull(ok)) {
            ok = OkHttpUtil.getDefault();
        }
        cookies.putAll(process());
        return this.cookies;
    }

    protected abstract Map<String, String> process();
}
