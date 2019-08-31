package com.chuyan.tangshianalyze.crawler.common;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Author： chuyan
 * Date：2019/6/12
 */
@Data
public class Page {
    /**
     * 数据网站的根地址
     * https://so.gushiwen.org
     */

    private final String base;

    /**
     * https://so.gushiwen.org/gushi/tangshi.aspx
     * 网页的具体路径
     * 比如/gushi/tangshi.aspx
     */
    private final String path;

    /**
     * 网页的DOM对象 ---文档对象模型
     */
    private HtmlPage htmlPage;


    /**
     * 标识网页是否是详情页
     */
    private final boolean detail;

    /**
     * 子页面对象集合
     * 如果detail是true  这个集合就为空
     */
    private Set<Page> subPage = new HashSet<> (  );

    /**
     * 数据对象
     *
     */
    private DataSet dataSet = new DataSet ();

    public String getUrl(){
        return this.base+this.path;
    }

}
