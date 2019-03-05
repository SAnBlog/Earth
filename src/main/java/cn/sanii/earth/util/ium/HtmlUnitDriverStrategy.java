package cn.sanii.earth.util.ium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


/**
 * HtmlUnitDriver 驱动策略
 * @author Administrator
 */
public class HtmlUnitDriverStrategy implements DriverStrategy {
    @Override
    public WebDriver getDriver(DriverEnum driverEnum) {
        // 如果页面的数据是通过js渲染的，改成true。（性能会降低）
        return new HtmlUnitDriver(false);
    }
}
