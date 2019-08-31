package com.chuyan.tangshianalyze.analyze.service;

import com.chuyan.tangshianalyze.analyze.model.AuthorCount;
import com.chuyan.tangshianalyze.analyze.model.WordCount;

import java.util.List;

/**
 * Author： chuyan
 * Date：2019/6/16
 */
public interface AnalyzeService {

    /**
     * 1.分析每个诗人的创作数量--->查询结果满足了
     * @return
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * 词云分析
     * 2.分析整个诗文中的词频--> 对查询结果进行分词、统计
     * @return
     */
    List<WordCount> analyzeWordCloud();
}
