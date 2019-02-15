# Earth

#### 介绍
流浪地球，分布式抓取框架雏形。

#### 软件架构

架构参考大名鼎鼎框架 webmagic
https://gitee.com/flashsword20/webmagic

#### 安装教程

1. JDK1.8
2. maven

#### 使用说明

快速入门

抓取捧腹网时所有页面笑话并持久化到本地

```
        Earth.me(new PengfueProcessor())//抓取页面提取规则
                .addUrl("https://www.pengfue.com/")//开始的种子
                .start();
```
完整功能

```
        Earth.me(new PengfueProcessor())//抓取页面提取规则
                .addUrl("https://www.pengfue.com/")//开始的种子
                .setPipelines(new SaveFilePipeline())//持久化到本地接口，可不用实现，默认打印控制台
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))//增加事件 对于请求做一些特殊处理，可不实现
                .start();
```


PengfueProcessor实现

```
public class PengfueProcessor implements Processor {

    @Override
    public void process(Response response) {
        final ResultField resultField = response.getResultField();

        Document document = response.getDocument();
        Elements nextPages = document.getElementsByClass("page").first().getElementsByClass("on");
        nextPages.forEach(element -> {
            if (element.text().contains("下一页")) {
                String attr = element.attr("href");
                log.info("next page:{}", attr);
                resultField.getRequests().add(new Request(attr));
            }
        });

        List<String> titles = Lists.newArrayList();
        List<String> contents = Lists.newArrayList();
        document.getElementsByClass("list-item").forEach(element -> {
            String title = element.getElementsByClass("dp-b").first().getElementsByTag("a").html();
            String val = element.getElementsByClass("content-img").first().html();
            titles.add(title);
            contents.add(val);
        });

        resultField.getFields().put("title", titles);
        resultField.getFields().put("contents", contents);

    }

    @Override
    public String name() {
        return "Pengfue";
    }

}
```

或者


```
Earth.me(new Processor() {
            @Override
            public void process(Response response) {
                final ResultField resultField = response.getResultField();

                Document document = response.getDocument();
                Elements nextPages = document.getElementsByClass("page").first().getElementsByClass("on");
                nextPages.forEach(element -> {
                    if (element.text().contains("下一页")) {
                        String attr = element.attr("href");
                        resultField.getRequests().add(new Request(attr));
                    }
                });

                List<String> titles = Lists.newArrayList();
                List<String> contents = Lists.newArrayList();
                document.getElementsByClass("list-item").forEach(element -> {
                    String title = element.getElementsByClass("dp-b").first().getElementsByTag("a").html();
                    String val = element.getElementsByClass("content-img").first().html();
                    titles.add(title);
                    contents.add(val);
                });

                resultField.getFields().put("title", titles);
                resultField.getFields().put("contents", contents);
            }

            @Override
            public String name() {
                return "Pengfue";
            }
        })
                .addUrl("https://www.pengfue.com/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .start();
```
