package com.chuyan.tangshianalyze.crawler;

import com.chuyan.tangshianalyze.crawler.common.Page;
import com.chuyan.tangshianalyze.crawler.parse.DataPageParse;

import com.chuyan.tangshianalyze.crawler.parse.DocumentParse;
import com.chuyan.tangshianalyze.crawler.parse.Parse;
import com.chuyan.tangshianalyze.crawler.pipeline.ConsolePipeline;
import com.chuyan.tangshianalyze.crawler.pipeline.Pipeline;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author： chuyan
 * Date：2019/6/12
 */
public class Crawler {

//    public static void main(String[] args) throws IOException {
//        final Page page = new Page ("https://so.gushiwen.org", "/gushi/tangshi.aspx",false );
//
//        WebClient webClient = new WebClient ( BrowserVersion.CHROME );
//        webClient.getOptions ().setJavaScriptEnabled ( false );
//        HtmlPage htmlPage =  webClient.getPage ( page.getUrl () );
//        page.setHtmlPage ( htmlPage );
//
////        Parse parse = new DataPageParse ();
////        parse.parse ( (page) );
////        Pipeline pipeline = new ConsolePipeline ();
////        pipeline.pipeline ( page );
//        Parse parse = new DocumentParse ();
//        parse.parse ( (page) );
//    }


    private final Logger logger = LoggerFactory.getLogger ( Crawler.class );

    /**
     * 生产着消费者模式----LinkedBlockingDeque
     * 放置采集的页面（文档页面、详情页面），先采集文档页面的子链接，然后再对子链接采集
     */
    private final Queue<Page> docQueue = new LinkedBlockingDeque<> (  );

    /**
     * 放置详情页面，用于清洗pipeline
     */
    private final Queue<Page> detailQueue = new LinkedBlockingDeque<>();

    /**
     * 采集器--htmlunit工具
     */
    private final WebClient webClient;

    /**
     * 所有的解析器(文档、详情2个)
     */
    private final List<Parse> parseList = new ArrayList<> (  );

    /**
     * 所有的清洗器（管道）
     */
    private final List<Pipeline> pipelineList = new ArrayList<> (  );

    /**
     * 线程调度器
     */
    private final ExecutorService executorService;

    public Crawler() {
        this.webClient = new WebClient ( BrowserVersion.CHROME );
        this.webClient.getOptions ().setJavaScriptEnabled ( false );
        this.executorService = Executors.newFixedThreadPool ( 8, new ThreadFactory () {

            private final AtomicInteger id = new AtomicInteger ( 0 );

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread ( r );
                thread.setName ( "Crawler-Thread-"+id.getAndIncrement () );
                return thread;
            }
        } );
    }

    public void start(){
        /**
         * 爬取
         * 解析
         * 清洗
         */
        this.executorService.submit(this::parse);
        this.executorService.submit(this::pipeline);
//        this.executorService.submit ( new Runnable () {
//            @Override
//            public void run() {
//                parse ();
//            }
//        } );

//        this.executorService.submit ( new Runnable () {
//            @Override
//            public void run() {
//                pipeline ();
//            }
//        } );
    }

    private void parse(){
        while (true){
            try {
                Thread.sleep ( 1000 );
            } catch (InterruptedException e) {
               // e.printStackTrace ();
                //{} 代表占位符 e.getMessage 打印进{}里
                logger.error ( "Parse occur exception {} ." ,e.getMessage ());
            }
            final Page page = this.docQueue.poll ();
            if(page==null){
                continue;
            }
            this.executorService.submit ( ()->{
                System.out.println (Thread.currentThread ().getName ());
                try{
                    //采集
                    HtmlPage htmlPage = Crawler.this.webClient.getPage ( page.getUrl () );
                    page.setHtmlPage ( htmlPage );

                    for(Parse parse:Crawler.this.parseList){
                        parse.parse ( page );
                    }
                    if(page.isDetail ()){
                        Crawler.this.detailQueue.add ( page );
                    }else {
                        Iterator<Page> iterator = page.getSubPage ().iterator ();
                        while (iterator.hasNext ()){
                            Page subPage = iterator.next ();
                            Crawler.this.docQueue.add ( subPage );
                          // System.out.println (subPage);
                            iterator.remove ();//remove--多线程
                        }
                    }
                }catch(IOException e){
                   // e.printStackTrace ();
                    logger.error("Parse task occur exception {} .", e.getMessage());
                }
            } );
        }
    }

    public void addPage(Page page){
        this.docQueue.add ( page );
    }

    public void addParse(Parse parse){
        this.parseList.add ( parse );
    }

    public void addPipeline(Pipeline pipeline){
        this.pipelineList.add ( pipeline );
    }

    private void pipeline(){
        while (true){
            try {
                Thread.sleep ( 1000 );
            } catch (InterruptedException e) {
               // e.printStackTrace ();
                logger.error ( "Parse occur exception {} ." ,e.getMessage ());
            }
            final Page page = this.detailQueue.poll ();
            if(page==null){
                continue;
            }
            this.executorService.submit ( ()->{
                for(Pipeline pipeline:Crawler.this.pipelineList){
                    pipeline.pipeline ( page );
                }
            } );
        }
    }

    public void stop(){
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
        logger.info ( "Crawler stopped...");
    }

//    public static void main(String[] args) throws IOException{
//        final Page page = new Page ( "https://so.gushiwen.org",
//                "/gushi/tangshi.aspx", false);
//
//        Crawler crawler = new Crawler ();
//        crawler.addParse ( new DocumentParse () );
//        crawler.addParse ( new DataPageParse () );
//        crawler.addPipeline ( new ConsolePipeline () );
//        crawler.addPage ( page );
//        crawler.start ();
//
//    }
}
