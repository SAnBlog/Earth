## git

地址：[Earth](https://gitee.com/SAnSAni/Earth)

## 特性

一个简单灵活的Java爬虫框架,可以快速开发出一个高效、易维护的爬虫。

*   简单的API，可快速上手
*   模块化的结构，可轻松扩展
*   提供多线程和分布式支持

### 示例

简单的实现，就能把整站妹子图下载到本地！

简单来说，你只需要实现Processor接口，开发目标页面的页面提取规则以及后续抓取种子的获取规则即可。
#### 同步启动
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
#### 异步启动
```js
        BaseComponent component = EventConfig.create(new MzituProcessor())
                .addUrl("https://www.mzitu.com/zipai/")
                .setPipelines(new SaveFilePipeline())
                .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                .thread(10);

        Earth.asyn(component);
```  

## 设计思想

开发的初衷也是为了了解和学习分布式爬虫框架的实现以及思想，刚开始入门爬虫使用Scrapy，初学懵懂无知，并不去深究实现与思想，单纯为了爬妹子图而学习。

但是在工作中用了一年的抓取，平时也是使用各种类库，重复搬砖劳动，萌发了了解常用爬虫架构的实现以及思想。在码云中搜索，翻阅了几遍非常优秀框架之一webmagic的源码后，打算自己开发一个，当然。。。仅仅是一个能运行的架子，很多扩展还需要后期慢慢扩展。

（这里webmagic也是参考了业界最优秀的爬虫Scrapy ）作者文档有说

[http://webmagic.io/docs/zh/posts/ch1-overview/thinking.html](http://webmagic.io/docs/zh/posts/ch1-overview/thinking.html)

同时还阅读了biezhi大佬的爬虫框架elves。结合了两者后产生的一个畸形产物。。。

这里也是借鉴了webmagic的那句话

Earth是一个无须配置、便于二次开发的爬虫框架，它提供简单灵活的API，只需少量代码即可实现一个爬虫。

## 总体架构

（架构参考的WebMagic，以下为WebMagic的资料）

WebMagic的结构分为Downloader、PageProcessor、Scheduler、Pipeline四大组件，并由Spider将它们彼此组织起来。这四大组件对应爬虫生命周期中的下载、处理、管理和持久化等功能。WebMagic的设计参考了Scapy，但是实现方式更Java化一些。

而Spider则将这几个组件组织起来，让它们可以互相交互，流程化的执行，可以认为Spider是一个大的容器，它也是WebMagic逻辑的核心。

WebMagic总体架构图如下：

![alt](https://github.com/SAnBlog/Earth/blob/master/san.png)

Earth的四个组件

### 1.Downloader

Downloader负责从互联网上下载页面，以便后续处理。Earth默认使用了okhttp3作为下载工具。

### 2.Processor

Processor负责解析页面，抽取有用信息，以及发现新的链接。目前只支持Document

在这四个组件中，Processor对于每个站点每个页面都不一样，是需要使用者定制的部分。

### 3.Scheduler

Scheduler负责管理待抓取的URL，以及一些去重的工作。Earth默认提供了JDK的内存队列来管理URL，并用集合来进行去重。也支持使用Redis进行分布式管理。

除非项目有一些特殊的分布式需求，否则无需自己定制Scheduler。

### 4.Pipeline

Pipeline负责抽取结果的处理，包括计算、持久化到文件、数据库等。Earth默认提供了“输出到控制台”和“保存到文件”两种结果处理方案。

Pipeline定义了结果保存的方式，如果你要保存到指定数据库，则需要编写对应的Pipeline。对于一类需求一般只需编写一个Pipeline。

用于数据流转的对象

### 1\. Request

Request是对URL地址的一层封装，一个Request对应一个URL地址。

它是Processor与Downloader交互的载体，也是Processor控制Downloader唯一方式。

除了URL本身外，它还包含一个Key-Value结构的字段extra。你可以在extra中保存一些特殊的属性，然后在其他地方读取，以完成不同的功能。例如附加上一个页面的一些信息等。

### 2\. Response

Page代表了从Downloader下载到的一个页面——可能是HTML，也可能是JSON或者其他文本格式的内容。

Page是WebMagic抽取过程的核心对象，它提供一些方法可供抽取、结果保存等。在第四章的例子中，我们会详细介绍它的使用。

### 3\. ResultItems

ResultItems相当于一个Map，它保存Processor处理的结果，供Pipeline使用。它的API与Map很类似，值得注意的是它有一个字段skip，若设置为true，则不应被Pipeline处理。

### 控制爬虫运转的引擎--Wandering

Wandering是Earth内部流程的核心。Downloader、Processor、Scheduler、Pipeline都是Wandering的一个属性，这些属性是可以自由设置的，通过设置这个属性可以实现不同的功能。Wandering也是Earth操作的入口，它封装了爬虫的创建、启动、停止、多线程等功能。下面是一个设置各个组件，并且设置多线程和启动的例子。

        public static void main(String[] args) {
            Earth.me(new MzituProcessor())
                    .addUrl("https://www.mzitu.com/zipai/")
                    .setPipelines(new SaveFilePipeline())
                    .addEvent(request -> Objects.nonNull(request), request -> System.out.println("请求体：" + JSONObject.toJSONString(request)))
                    .thread(10)
                    .start();
        }

### 快速上手

上面介绍了很多组件，但是其实使用者需要关心的没有那么多，因为大部分模块Earth已经提供了默认实现。

一般来说，对于编写一个爬虫，Processor是需要编写的部分，而Spider则是创建和控制爬虫的入口。在第四章中，我们会介绍如何通过定制Processor来编写一个爬虫，并通过Spider来启动。
