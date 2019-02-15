package download;

import model.Request;
import model.Response;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 16:58
 * @Description: 下载请求接口
 */
public interface Downloader {

    Response download(Request request);

}
