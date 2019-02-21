package cn.sanii.earth.process;

import cn.sanii.earth.model.Response;
import cn.sanii.earth.model.enums.FieldEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Administrator
 * @create: 2019/2/21
 * Description: 下载页面所有图片
 */
public abstract class BaseImgProcessor implements IProcessor {

    private String prifex;

    /**
     * 匹配字符串中的img标签
     */
    private final static Pattern P = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");
    private final static Pattern SRC_TEXT = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");

    /**
     * 提取所有图片
     *
     * @param response
     */
    @Override
    public void process(Response response) {
        List<String> srcList = Lists.newArrayList();
        Matcher matcher = P.matcher(response.getHtml());
        boolean hasPic = matcher.find();
        if (hasPic == true) {
            while (hasPic) {
                String group = matcher.group(2);
                Matcher matcher2 = SRC_TEXT.matcher(group);
                if (matcher2.find()) {
                    String url = matcher2.group(3);
                    if (StringUtils.isNotBlank(this.prifex) && url.contains(this.prifex)) {
                        srcList.add(url);
                    } else {
                        srcList.add(url);
                    }
                }
                hasPic = matcher.find();
            }
        }
        Map<String, String> urlMaps = Maps.newHashMap();
        srcList.forEach(s -> urlMaps.put(System.nanoTime() + "", s));
        response.getResultField().getFields().put(FieldEnum.BYTE, urlMaps);
    }

    /**
     * 设置允许下载图片前缀
     *
     * @param prifex
     * @return
     */
    public void setPrifex(String prifex) {
        this.prifex = prifex;
    }
}
