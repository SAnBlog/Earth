package cn.sanii.earth.util.ium;

import org.openqa.selenium.WebDriver;

/**
 * 驱动策略接口，根据不同枚举使用不同驱动
 * @author Administrator
 */
public interface DriverStrategy {

    /**
     *
     * @param driverEnum 驱动枚举
     * @return 获取驱动
     */
    WebDriver getDriver(DriverEnum driverEnum);
}
