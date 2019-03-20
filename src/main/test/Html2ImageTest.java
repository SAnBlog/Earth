import cn.sanii.earth.util.Html2Image.CssboxUtil;
import cn.sanii.earth.util.Html2Image.Html2ImageUtil;
import cn.sanii.earth.util.Html2Image.PrintScreen4DJNativeSwingUtils;
import cn.sanii.earth.util.OkHttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author: Administrator
 * @create: 2019/3/19
 * Description:
 */
public class Html2ImageTest {
    private static final OkHttpClient client = OkHttpUtil.getDefault();

    public static final String URL = "http://www.travelsky.com/tsky/validate.jsp";

    public static void main(String[] args) throws IOException, SAXException {
//        Html2Image();
//        JWebBrowser();
        cssbox();
    }

    public static void Html2Image() throws IOException {
        Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();
        String html = client.newCall(request).execute().body().string();

        Html2ImageUtil.html2Img(html, "D:\\2_" + System.currentTimeMillis() + ".png");
    }

    public static void JWebBrowser() throws IOException {
        PrintScreen4DJNativeSwingUtils.printUrlScreen2jpg("D:\\3_" + System.currentTimeMillis() + ".png", "http://www.travelsky.com/tsky/validate.jsp", 0, 0);
    }

    public static void cssbox() throws IOException, SAXException {
        String url = "http://www.travelsky.com/tsky/validate.jsp";
        CssboxUtil.toPath(url, "D:\\4_" + System.currentTimeMillis() + ".png");
        System.out.println(CssboxUtil.toByte(url).length);
    }


}
