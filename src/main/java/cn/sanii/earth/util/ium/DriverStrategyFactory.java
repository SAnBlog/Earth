package cn.sanii.earth.util.ium;

import org.openqa.selenium.WebDriver;

/**
 * 驱动策略工厂
 *
 * @author Administrator
 */
public class DriverStrategyFactory {


    /**
     * 获取策略驱动
     *
     * @param driverEnum 枚举驱动
     * @link me.liao.gecco.selenium.utils.DriverEnum
     * @return
     */
    public static WebDriver getInstance(DriverEnum driverEnum) {
        switch (driverEnum) {
            case HtmlUnit:
                return new HtmlUnitDriverStrategy().getDriver(driverEnum);
            case PhantomJS:
                return new PhantomJSDriverStrategy().getDriver(driverEnum);
            case Chrome:
                return new ChromeDriverStrategy().getDriver(driverEnum);
            default:
                return null;
        }
    }
}
