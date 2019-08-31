package com.chuyan.tangshi;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

/**
 * Author： chuyan
 * Date：2019/5/30
 */
public class TestHtmlUnit {
    public static void main(String[] args) {
//模拟浏览器
        try(WebClient webClient = new WebClient ( BrowserVersion.CHROME )){

            //不执行js
            webClient.getOptions ().setJavaScriptEnabled ( false );

            HtmlPage htmlPage =  webClient.getPage ( "https://so.gushiwen.org/shiwenv_45c396367f59.aspx" );

//            HtmlElement body = htmlPage.getBody ();
//            String titlePath = "//div[@class='cont']/h1/text()";//h1的text元素
//            DomText titleDom = (DomText) body.getByXPath ( titlePath ).get ( 0 );
//            System.out.println (titleDom.asText ());


            //        DomText titleDom = (DomText) body.getByXPath ( titlePath ).get(0);
//        String title = titleDom.asText ();//取出文本
//        System.out.println (title);

//            HtmlElement bodyElement = htmlPage.getBody ();//element-元素  body-标签
//            String text = bodyElement.asText ();//asText()只是取出文本  asXml()把结构（节点）也取出来
//            System.out.println (text);

//          HtmlDivision domElement = (HtmlDivision) htmlPage.getElementById ( "contsonedcfaa024991" );
//          //  System.out.println (domElement.getClass ().getName ());
//            String str = domElement.asText ();
//            System.out.println (str);
        }catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
