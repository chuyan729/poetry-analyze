package com.chuyan.tangshianalyze.crawler.parse;

import com.chuyan.tangshianalyze.crawler.common.Page;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 文档页面的解析（非详情页面）
 * Author： chuyan
 * Date：2019/6/14
 */
public class DocumentParse implements Parse {
    @Override
    public void parse(final Page page) {
        if(page.isDetail ()){
            return;
        }
       // final AtomicInteger count = new AtomicInteger ( 0 );
//        HtmlPage htmlPage = page.getHtmlPage ();
//        htmlPage.getBody ().getElementsByAttribute (
//                "div",
//                "class",
//                "typecont" )
//                .forEach ( htmlElement -> {
//                   DomNodeList<HtmlElement> nodeList =  htmlElement.getElementsByTagName ( "a" );
//                   nodeList.forEach (
//                           aNode -> {
//                              String path = aNode.getAttribute ( "href" );
//                             // count.getAndIncrement ();
//                             //  System.out.println (path);
//                               Page subPage = new Page (
//                                       page.getBase (),
//                                       path,
//                                       true);
//                               page.getSubPage ().add ( subPage );
//                   } );
//                } );
       // System.out.println (count);

//----------------------------------------------------
        HtmlPage htmlPage = page.getHtmlPage ();
        htmlPage.getBody ()
                .getElementsByAttribute ("div","class","typecont"  )
                .forEach ( div->{
                    DomNodeList<HtmlElement> aNodeList =  div.getElementsByTagName ( "a" );
                    aNodeList.forEach ( aNode ->{
                        String path = aNode.getAttribute ( "href" );
                        Page subPage = new Page ( page.getBase (),path,true );
                        page.getSubPage ().add ( subPage );
                    } );

                } );

    }
}
