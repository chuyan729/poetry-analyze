package com.chuyan.tangshianalyze.crawler.pipeline;

import com.chuyan.tangshianalyze.crawler.common.Page;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Author： chuyan
 * Date：2019/6/12
 */
public class ConsolePipeline implements Pipeline {
    @Override
    public void pipeline(final Page page) {
        Map<String,Object> data = page.getDataSet ().getData ();
        //存储
        System.out.println (data );
        //System.out.println(System.currentTimeMillis());
    }
}
