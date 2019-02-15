package process;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 19:15
 * @Description:
 */
@Data
@Slf4j
public class SaniiBeforeProcessor extends BeforeProcessor {

    @Override
    protected Map<String, String> process() {
        FormBody body = new FormBody.Builder()
                .add("username", "san")
                .add("password", "1300100082San")
                .add("remeberMe", "on")
                .build();

        try {
            Request build = new Request.Builder().url("https://sanii.cn/admin/login").post(body).build();
            Response response = ok.newCall(build).execute();
            response.headers("Set-Cookie").forEach(s -> {
                String[] split = s.split(";")[0].split("=");
                cookies.put(split[0], split[1]);
            });
        } catch (IOException e) {
            log.error("SaniiBeforeProcessor error", e);
        }
        return cookies;
    }
}
