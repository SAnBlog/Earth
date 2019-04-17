package cn.sanii.earth.util.Html2Image;

import org.fit.cssbox.demo.ImageRenderer;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: shouliang.wang
 * @create: 2019-03-20 13:17
 * @description: Cssbox html生成图片工具类
 **/
public class CssboxUtil {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 550;

    /**
     * 图片生成到指定路径
     * 支持 file ftp http https
     *
     * @param url  html地址
     * @param path 生成文件地址
     * @throws IOException
     * @throws SAXException
     */
    public static void toPath(String url, String path) throws IOException, SAXException {
        toPath(url, path, WIDTH, HEIGHT);
    }

    /**
     * 图片生成到指定路径
     * 支持 file ftp http https
     *
     * @param url  html地址
     * @param path 生成文件地址
     * @throws IOException
     * @throws SAXException
     */
    public static void toPath(String url, String path, int width, int height) throws IOException, SAXException {
        ImageRenderer render = new ImageRenderer();
        FileOutputStream out = new FileOutputStream(new File(path));
        render.setWindowSize(new Dimension(width, height), false);
        render.renderURL(url, out, ImageRenderer.Type.PNG);
        out.close();
    }

    /**
     * 图片生成到byte数组
     * 支持 file ftp http https
     *
     * @param url html地址
     * @throws IOException
     * @throws SAXException
     */
    public static byte[] toByte(String url) throws IOException, SAXException {
        return toByte(url, WIDTH, HEIGHT);
    }

    /**
     * 图片生成到byte数组
     * 支持 file ftp http https
     *
     * @param url html地址
     * @throws IOException
     * @throws SAXException
     */
    public static byte[] toByte(String url, int width, int height) throws IOException, SAXException {
        ImageRenderer render = new ImageRenderer();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        render.setWindowSize(new Dimension(1900, 1000), false);
        render.renderURL(url, stream, ImageRenderer.Type.PNG);
        byte[] bytes = stream.toByteArray();
        stream.close();
        return bytes;
    }
}
