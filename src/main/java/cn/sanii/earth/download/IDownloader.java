package cn.sanii.earth.download;

import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/13 16:58
 * @Description: 下载请求接口
 */
public interface IDownloader {

    Response download(Request request);

}
