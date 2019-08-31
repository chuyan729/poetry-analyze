package com.chuyan.tangshianalyze.crawler.parse;

import com.chuyan.tangshianalyze.crawler.common.Page;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * 详情页面解析
 * Author： chuyan
 * Date：2019/6/12
 */
public class DataPageParse implements Parse{

    @Override
    public void parse(final Page page) {
        if(!page.isDetail ()) {
            return;
        }

//        HtmlPage htmlPage = page.getHtmlPage ();
//        HtmlElement body = htmlPage.getBody ();
//        //标题
//        String titlePath = "//div[@class='cont']/h1/text()";//h1的text元素
//
//        Object o =  htmlPage.getByXPath ( titlePath ).get ( 0 );
//        System.out.println (o.getClass ().getName ());


        HtmlPage htmlPage = page.getHtmlPage ();
        HtmlElement body = htmlPage.getBody ();

        /**
         * 可以利用 System.out.println(o.getClass().getName())
         *    得到类型
         */

        //标题
        String titlePath = "//div[@class='cont']/h1/text()";//h1(标题标签)的text元素
        DomText titleDom = (DomText) body.getByXPath ( titlePath ).get(0);
        String title = titleDom.asText ();//取出文本
     //   System.out.println (title);

        //朝代
        String dynastyPath = "//div[@class='cont']/p/a[1]";
        HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath ( dynastyPath ).get(0);
        String dynasty = dynastyDom.asText ();

        //作者
        String authorPath = "//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();

        //正文----改正id可以吗
        String contentPath = "//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();

        page.getDataSet().putData("title",title);
        page.getDataSet().putData("dynasty",dynasty);
        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);
        //更多的数据

//        PoetryInfo poetryInfo = new PoetryInfo ();
//        poetryInfo.setDynasty ( dynasty );
//        poetryInfo.setAuthor ( author );
//        poetryInfo.setTitle ( title );
//        poetryInfo.setContent ( content );
//
//        page.getDataSet ().putData ( "poetry",poetryInfo );
    }
}
