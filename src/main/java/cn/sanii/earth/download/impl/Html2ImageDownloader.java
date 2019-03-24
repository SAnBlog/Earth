package cn.sanii.earth.download.impl;

import cn.sanii.earth.download.IDownloader;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.util.Html2Image.CssboxUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Administrator
 * @create: 2019/3/24
 * Description: html生成图片字节
 */
@Slf4j
public class Html2ImageDownloader implements IDownloader {

    @Override
    public Response download(Request request) {
        Response response = new Response();
        response.setSuccess(false).setRequest(request);
        try {
            byte[] bytes = CssboxUtil.toByte(request.getUrl());
            response.setBytes(bytes);
            response.setSuccess(true);
        } catch (Exception e) {
            log.error("Html2ImageDownloader download error", e);
        }
        return response;
    }
}
