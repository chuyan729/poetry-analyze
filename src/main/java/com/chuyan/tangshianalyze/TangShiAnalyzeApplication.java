package com.chuyan.tangshianalyze;

import com.alibaba.druid.pool.DruidDataSource;
import com.chuyan.tangshianalyze.analyze.dao.AnalyzeDao;
import com.chuyan.tangshianalyze.analyze.dao.impl.AnalyzeDaoImpl;
import com.chuyan.tangshianalyze.analyze.service.AnalyzeService;
import com.chuyan.tangshianalyze.analyze.service.impl.AnalyzeServiceImpl;
import com.chuyan.tangshianalyze.config.ConfigProperties;
import com.chuyan.tangshianalyze.config.ObjectFactory;
import com.chuyan.tangshianalyze.crawler.Crawler;
import com.chuyan.tangshianalyze.crawler.common.Page;
import com.chuyan.tangshianalyze.crawler.parse.DataPageParse;
import com.chuyan.tangshianalyze.crawler.parse.DocumentParse;
import com.chuyan.tangshianalyze.crawler.pipeline.DataBasePipeline;
import com.chuyan.tangshianalyze.web.WebController;
import org.omg.CORBA.DATA_CONVERSION;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import spark.Spark;
import java.time.LocalDateTime;
import static spark.route.HttpMethod.get;

/**
 * 唐诗分析程序的主类
 * Author： chuyan
 * Date：2019/5/30
 */
public class TangShiAnalyzeApplication {


    private static final Logger LOGGER = LoggerFactory.getLogger ( TangShiAnalyzeApplication.class );
    public static void main(String[] args) {
        //1
//       Crawler crawler = ObjectFactory.getInstance ().getObject ( Crawler.class );
//       crawler.start ();

        // 2
//        Spark.get("/hello",(req, resp) -> {
//            return "hello crawler"+LocalDateTime.now ().toString ();
//        });

        WebController   webController = ObjectFactory.getInstance ()
                .getObject ( WebController.class );

        //运行web服务，提供接口
        LOGGER.info ( "Web Server Launch..." );
        webController.launch ();

        //启动爬虫
        if(args.length==1 && args[0].equals ( "run_crawler" )) {
            Crawler crawler = ObjectFactory.getInstance ().getObject ( Crawler.class );
            LOGGER.info ( "Crawler start..." );
            crawler.start ();
        }
    }


//
//    public static void main(String[] args) {
//
//        ConfigProperties configProperties = new ConfigProperties ();
//
////        final Page page = new Page ( configProperties.getCrawlerBase (),
////                configProperties.getCrawlerPath (),
////                configProperties.isCrawlerDetail());
////
////        Crawler crawler = new Crawler ();
////        crawler.addParse ( new DocumentParse () );
////        crawler.addParse ( new DataPageParse () );
////
//        //crawler.addPipeline ( new ConsolePipeline () );
//
//        DruidDataSource dataSource = new DruidDataSource (  );
//        dataSource.setUsername ( configProperties.getDbUsername () );
//        dataSource.setPassword ( configProperties.getDbPassword ());
//        dataSource.setDriverClassName ( configProperties.getDbDriverClass () );
//        dataSource.setUrl ( configProperties.getDbUrl () );
//
////        crawler.addPipeline ( new DataBasePipeline ( dataSource ) );
////
////        crawler.addPage ( page );
////        crawler.start ();
//
//        AnalyzeDao analyzeDao = new AnalyzeDaoImpl (dataSource );
///*
//        System.out.println ("测试1");
//        analyzeDao.analyzeAuthorCount ()
//                .forEach ( authorCount -> {
//                    System.out.println (authorCount);
//                } );
//
//        System.out.println ("测试2");
//        analyzeDao.queryPortryInfo ()
//                .forEach ( poetryInfo -> {
//                    System.out.println (poetryInfo);
//                } );
//*/
//        AnalyzeService analyzeService = new AnalyzeServiceImpl (analyzeDao);
//
////        analyzeService.analyzeAuthorCount ()
////                .forEach ( authorCount -> {
////                    System.out.println (authorCount);
////                } );
//
//        analyzeService.analyzeWordCloud ().forEach ( wordCount -> {
//            System.out.println (wordCount);
//        } );
//    }

}
