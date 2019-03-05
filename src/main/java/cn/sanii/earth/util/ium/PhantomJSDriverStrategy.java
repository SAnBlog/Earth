package cn.sanii.earth.util.ium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * PhantomJSDriver驱动策略
 *
 * @author Administrator
 */
public class PhantomJSDriverStrategy implements DriverStrategy {
    @Override
    public WebDriver getDriver(DriverEnum driverEnum) {
        String phabtomjs = PhantomJSDriverStrategy.class.getClassLoader().getResource(driverEnum.getDes()).toString();
        if (phabtomjs.indexOf("file:/") == 0) {
            phabtomjs = phabtomjs.substring(6);
        }
        System.setProperty("phantomjs.binary.path", phabtomjs);
        return new PhantomJSDriver();
    }
}
