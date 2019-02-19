# Earth

#### 介绍
[框架文档](https://sanii.cn/article/282)

#### 软件架构

架构参考大名鼎鼎框架 webmagic
https://gitee.com/flashsword20/webmagic

#### 安装教程

1. JDK1.8
2. maven

#### 使用说明

#### 示例
抓取妹子图自拍区所有图片并下载到本地

同步方式
```
public class MzituProcessor implements Processor {

    
    @Override
    public void process(Response response) {
        Document document = response.getDocument();

        //获取下一个种子地址
        document.getElementsByClass("pagenavi-cm").first().getElementsByTag("a").forEach(a -> {
            if (Objects.nonNull(a) && a.text().contains("下一页")) {
                String nextPage = a.attr("href");
                response.getResultField().getRequests().add(new Request(nextPage));
            }
        });

        /**
         * 图片地址提取规则
         */
        Map<String, String> images = Maps.newHashMap();
        document.getElementsByClass("lazy").forEach(element -> {
            String img = element.attr("data-original");
            images.put(System.currentTimeMillis() + "", img);
        });

        response.getResultField().getFields().put(FieldEnum.BYTE, images);
    }

    @Override
    public String name() {
        return "Mzitu";
    }

    public static void main(String[] args) {
        Earth.me(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())//保存到本地管道
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))//打印请求到控制台 可省略
                .start();
    }
}
```

异步

```$xslt
        BaseComponent component = EventConfig.create(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .thread(10);

        Earth.asyn(component);
```
