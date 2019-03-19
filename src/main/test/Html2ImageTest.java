import cn.sanii.earth.util.Html2Image.Html2ImageUtil;
import cn.sanii.earth.util.Html2Image.PrintScreen4DJNativeSwingUtils;
import cn.sanii.earth.util.OkHttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.fit.cssbox.demo.ImageRenderer;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
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
        Html2Image();
        JWebBrowser();
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
        ImageRenderer render = new ImageRenderer();
        System.out.println("start...");
        String url = "http://www.travelsky.com/tsky/validate.jsp";
        FileOutputStream out = new FileOutputStream(new File("D:\\4_" + System.currentTimeMillis() + ".png"));
        render.setWindowSize(new Dimension(1900, 1000), false);
        render.renderURL(url, out, ImageRenderer.Type.PNG);
        out.close();
        System.out.println("OK");
    }


}
