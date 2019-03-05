package cn.sanii.earth.download.impl;

import cn.sanii.earth.download.IDownloader;
import cn.sanii.earth.model.Request;
import cn.sanii.earth.model.Response;
import cn.sanii.earth.util.ium.DriverEnum;
import cn.sanii.earth.util.ium.DriverStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * @author: Administrator
 * @create: 2019/3/5
 * Description:
 */
@Slf4j
public class PhantomJSDownloader implements IDownloader {

    private static WebDriver driver = DriverStrategyFactory.getInstance(DriverEnum.PhantomJS);

    @Override
    public Response download(Request request) {
        Response response = new Response();
        response.setSuccess(false).setRequest(request);

        try {
            driver.get(request.getUrl());
            String result = driver.getPageSource();
            response.setResult(result);
            response.setSuccess(true);
        } catch (Exception e) {
            log.error("error",e);
        }
        return response;
    }
}
