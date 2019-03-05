package cn.sanii.earth.util.ium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * ChromeDriver驱动策略
 *
 * @author Administrator
 */
public class ChromeDriverStrategy implements DriverStrategy {
    @Override
    public WebDriver getDriver(DriverEnum driverEnum) {
        String chromedriver = ChromeDriverStrategy.class.getClassLoader().getResource(driverEnum.getDes()).toString();
        if (chromedriver.indexOf("file:/") == 0) {
            chromedriver = chromedriver.substring(6);
        }
        System.setProperty("webdriver.chrome.driver", chromedriver);
        return new ChromeDriver();
    }
}
