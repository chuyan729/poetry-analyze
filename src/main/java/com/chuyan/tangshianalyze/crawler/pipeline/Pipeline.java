package com.chuyan.tangshianalyze.crawler.pipeline;

import com.chuyan.tangshianalyze.crawler.common.Page;

/**
 * 清洗
 * Author： chuyan
 * Date：2019/6/12
 */
public interface Pipeline {
    void pipeline(final Page page);
}
