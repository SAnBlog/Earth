package cn.sanii.earth.pipeline;

import cn.sanii.earth.model.Response;
import cn.sanii.earth.util.GuavaThreadPoolUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/14 14:07
 * @Description: 持久化到文件
 */
@Slf4j
public class SaveFilePipeline implements Pipeline {

    private static ListeningExecutorService service;

    private String prifix;

    @Override
    public void process(Response response) {
        prifix = new StringBuilder(FILE_PATH).append(response.getName()).append("").toString();
        final File newFile = new File(prifix + (".txt"));
        response.getResultField().getFields().forEach((key, val) -> {
            try {
                switch (key) {
                    case TEXT:
                        String line = System.getProperty("line.separator");
                        Files.append((JSONObject.toJSONString(val) + line), newFile, Charset.defaultCharset());
                        break;
                    case BYTE:
                        if (val instanceof Map) {
                            if (Objects.isNull(service)) {
                                service = GuavaThreadPoolUtils.getDefualtGuavaExecutor();
                            }
                            Map<String, String> urls = (Map<String, String>) val;
                            urls.forEach((k, v) -> {
                                service.submit(() -> {
                                    try {
                                        URL url = new URL(k);
                                        // 打开URL连接
                                        URLConnection con = url.openConnection();
                                        // 得到URL的输入流
                                        InputStream input = con.getInputStream();
                                        byte[] data = readInputStream(input);
                                        StringBuilder path = new StringBuilder(prifix).append("/");
                                        File file = new File(path.toString());
                                        if (!file.exists()) {
                                            file.mkdirs();
                                        }
                                        Files.write(data, new File(path.append(v).append(".").append(k.substring(k.lastIndexOf(".") + 1)).toString()));
                                    } catch (IOException e) {
                                        log.error("download error", e);
                                    }
                                });
                            });
                        }
                        break;
                    default:
                        log.error("不支持的持久化类型");
                }
            } catch (IOException e) {
                log.error("SaveFilePipeline eroror", e);
            }
        });
    }

    public static byte[] readInputStream(InputStream inStream) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            inStream.close();
            //把outStream里的数据写入内存
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outStream.toByteArray();
    }
}
