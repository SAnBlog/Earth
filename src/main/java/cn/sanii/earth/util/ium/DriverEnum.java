package cn.sanii.earth.util.ium;

/**
 * 驱动策略枚举
 */
public enum DriverEnum {

    HtmlUnit("HtmlUnitDriver", 1, "HtmlUnit无需设置路径"), PhantomJS("PhantomJSDriver", 2, "driver/phantomjs.exe"), Chrome("ChromeDriver", 3, "driver/chromedriver.exe");

    private String name;
    private int code;
    private String des;

    DriverEnum(String name, int code, String des) {
        this.name = name;
        this.code = code;
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
