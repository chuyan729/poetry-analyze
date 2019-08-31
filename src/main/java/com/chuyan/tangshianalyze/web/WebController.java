package com.chuyan.tangshianalyze.web;

import com.chuyan.tangshianalyze.analyze.model.AuthorCount;
import com.chuyan.tangshianalyze.analyze.model.WordCount;
import com.chuyan.tangshianalyze.analyze.service.AnalyzeService;
import com.chuyan.tangshianalyze.config.ObjectFactory;
import com.chuyan.tangshianalyze.crawler.Crawler;
import com.google.gson.Gson;
import spark.*;

import java.util.List;

/**
 * Web API
 * 1.Sparkjava框架完成Web API开发----可以将这一部分代码换成servlet
 * 2.Servlet实现
 * 3.Java-Httpd 实现  （纯java语言实现的web服务）
 *   Socket   要对Http协议非常清楚
 * Author： chuyan
 * Date：2019/6/17
 */
public class WebController {

    private final AnalyzeService analyzeService;

    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    // -> http://127.0.0.1:4567/..
    // -> /analyze/author_count
    private List<AuthorCount> analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount ();
    }

    // -> http://127.0.0.1:4567/..
    // -> /analyze/author_cloud
    private List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud ();
    }

    public void launch(){

        ResponseTransformer transformer =new JSONResponseTransformer ();

        //   src/main/resources/static
        //前端静态文件目录
        Spark.staticFileLocation ( "/static" );

        //服务器接口
        Spark.get ( "/analyze/author_count",
                ((request, response) -> analyzeAuthorCount ()),
                transformer
                );

        Spark.get ("/analyze/author_cloud",
                ((request, response) -> analyzeWordCloud ()),
                transformer
                );

        Spark.get ( "/crawler/stop",
                ((request, response) -> {
                   Crawler crawler = ObjectFactory.getInstance ().getObject ( Crawler.class );
                   crawler.stop ();
                   return "爬虫停止";
                })
                );
    }

    public static class JSONResponseTransformer implements ResponseTransformer{
        //object==>string
        private  Gson gson = new Gson ();

        @Override
        public String render(Object o) throws Exception {
            return gson.toJson ( o );
        }
    }

//    public static void main(String[] args) {
//        Gson gson = new Gson ();
//        //object==>string
//        WordCount wordCount = new WordCount ();
//        wordCount.setWord ( "java" );
//        wordCount.setCount ( 10 );
//        System.out.println (gson.toJson ( wordCount ));
//
//        //string==>object
//        String str = "{\"word\":\"java\",\"count\":10}";
//        WordCount wordCount1 = gson.fromJson ( str,WordCount.class );
//        System.out.println (wordCount1);
//    }
}
