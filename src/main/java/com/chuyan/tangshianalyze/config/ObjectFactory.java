package com.chuyan.tangshianalyze.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.chuyan.tangshianalyze.analyze.dao.AnalyzeDao;
import com.chuyan.tangshianalyze.analyze.dao.impl.AnalyzeDaoImpl;
import com.chuyan.tangshianalyze.analyze.service.AnalyzeService;
import com.chuyan.tangshianalyze.analyze.service.impl.AnalyzeServiceImpl;
import com.chuyan.tangshianalyze.crawler.Crawler;
import com.chuyan.tangshianalyze.crawler.common.Page;
import com.chuyan.tangshianalyze.crawler.parse.DataPageParse;
import com.chuyan.tangshianalyze.crawler.parse.DocumentParse;
import com.chuyan.tangshianalyze.crawler.pipeline.ConsolePipeline;
import com.chuyan.tangshianalyze.crawler.pipeline.DataBasePipeline;
import com.chuyan.tangshianalyze.web.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author： chuyan
 * Date：2019/6/16
 */
public class ObjectFactory {

    private final Logger logger = LoggerFactory.getLogger ( ObjectFactory.class );

    //单例--饿汉式
    private static final ObjectFactory instance = new ObjectFactory ();

    /**
     * 存放所有的对象
     */
    private  final Map<Class,Object> objectMap = new HashMap<> (  );

    private ObjectFactory(){
        //1.初始化配置对象
        initConfigProperties();

        //2.数据源对象
        initDataSource();

        //3.爬虫
        initCrawler();

        //4.Web对象
        initWebController();

        //5.对象清单打印输出
        printObjectList ();
    }

    private void initWebController() {
        DataSource dataSource = getObject ( DataSource.class );
        AnalyzeDao analyzeDao = new AnalyzeDaoImpl ( dataSource );
        AnalyzeService analyzeService = new AnalyzeServiceImpl ( analyzeDao );

        WebController webController = new WebController ( analyzeService );

        objectMap.put ( WebController.class,webController );

    }


    private void initCrawler() {
        DataSource dataSource = getObject ( DataSource.class );
        ConfigProperties configProperties = getObject ( ConfigProperties.class );
        final Page page = new Page ( configProperties.getCrawlerBase (),
                configProperties.getCrawlerPath (),
                configProperties.isCrawlerDetail());

                Crawler crawler = new Crawler ();
        crawler.addParse ( new DocumentParse () );
        crawler.addParse ( new DataPageParse () );

        if(configProperties.isEnableConsole ()) {
            crawler.addPipeline ( new ConsolePipeline () );
        }
        crawler.addPipeline ( new DataBasePipeline ( dataSource ) );

        crawler.addPage ( page );

        objectMap.put ( Crawler.class,crawler );
    }

    private void initDataSource() {
        ConfigProperties configProperties = getObject ( ConfigProperties.class );
        DruidDataSource dataSource = new DruidDataSource (  );
        dataSource.setUsername ( configProperties.getDbUsername () );
        dataSource.setPassword ( configProperties.getDbPassword ());
        dataSource.setDriverClassName ( configProperties.getDbDriverClass () );
        dataSource.setUrl ( configProperties.getDbUrl () );

        objectMap.put ( DataSource.class,dataSource );
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties ();

        objectMap.put ( ConfigProperties.class,configProperties );

        logger.info ( "ConfigProperties info:\n{}",configProperties.toString() );

    }

    //通过类返回该类的对象
    public  <T> T getObject(Class classz){
        if(!objectMap.containsKey ( classz )){
            throw new IllegalArgumentException ( "Class "
                    +classz.getName ()+"not found Object" );
        }
        return (T) objectMap.get ( classz );
    }

    public static ObjectFactory getInstance(){
        return instance;
    }

    private void printObjectList(){
       logger.info ("====ObjectFactory List====");
        for(Map.Entry<Class,Object> entry:objectMap.entrySet ()){
            logger.info (
                    String.format (
                    "\t[%s]===>[%s]",
                            entry.getKey ().getCanonicalName (),
                            entry.getValue ().getClass ().getCanonicalName ()
                    )
            );
        }
        logger.info ("====finish====");
    }

}
