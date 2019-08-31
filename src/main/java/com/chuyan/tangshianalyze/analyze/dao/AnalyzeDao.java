package com.chuyan.tangshianalyze.analyze.dao;

import com.chuyan.tangshianalyze.analyze.entity.PoetryInfo;
import com.chuyan.tangshianalyze.analyze.model.AuthorCount;

import java.util.List;

/**
 * Author： chuyan
 * Date：2019/6/16
 */
public interface AnalyzeDao {

    /**
     * 分析唐诗中每个作者创作诗的数量（等同于作者的数量）
     */
    List<AuthorCount> analyzeAuthorCount();

    /**
     * 查询所有的诗文，提供给业务层进行分析
     * @return
     */
    List<PoetryInfo> queryPortryInfo();
}
