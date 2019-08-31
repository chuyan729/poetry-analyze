package com.chuyan.tangshianalyze.analyze.service.impl;

import com.chuyan.tangshianalyze.analyze.dao.AnalyzeDao;
import com.chuyan.tangshianalyze.analyze.entity.PoetryInfo;
import com.chuyan.tangshianalyze.analyze.model.AuthorCount;
import com.chuyan.tangshianalyze.analyze.model.WordCount;
import com.chuyan.tangshianalyze.analyze.service.AnalyzeService;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;

/**
 * Author： chuyan
 * Date：2019/6/16
 */
public class AnalyzeServiceImpl implements AnalyzeService {

    //依赖数据库  dao层的查询结果
    private final AnalyzeDao analyzeDao;

    public AnalyzeServiceImpl(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }


    @Override
    public List<AuthorCount> analyzeAuthorCount() {

        /**
         * 此处结果并未排序，可以添加排序
         * 方式：
         * 1.在DAO层sql语句加order by count
         * 2.Service层进行排序 ---对 list 集合排序
         */
        List<AuthorCount> authorCounts = analyzeDao.analyzeAuthorCount ();

//        Collections.sort ( authorCounts, new Comparator<AuthorCount> () {
//            @Override
//            public int compare(AuthorCount o1, AuthorCount o2) {
//                    //升序    若想降序则乘-1
////                return o1.getCount ()-o2.getCount ();
//                return o1.getCount ().compareTo ( o2.getCount () );
//
//            }
//        } );

        authorCounts.sort ( Comparator.comparing ( AuthorCount::getCount ) );
        return authorCounts;

    }

    @Override
    public List<WordCount> analyzeWordCloud() {

        //1.查询出所有数据
        //2.取出 title content
        //3.分词--过滤  /W  空  lenth<2
        //4.统计 k-v  词-次数

        Map<String,Integer> map = new HashMap<> (  );

        List<PoetryInfo> poetryInfos = analyzeDao.queryPortryInfo ();
        for(PoetryInfo poetryInfo:poetryInfos){
            List<Term> terms = new ArrayList<> (  );

            //取出 ===  一首诗的标题和正文
            String title = poetryInfo.getTitle ();
            String content  = poetryInfo.getContent ();

            //分词 ===   对标题和正文进行分词，并存储到terms
            terms.addAll( NlpAnalysis.parse ( title ).getTerms () );
            terms.addAll ( NlpAnalysis.parse ( content ).getTerms () );

            //过滤，统计（存入map） === 过滤掉标点符号（w），过滤掉长度小于2的
            Iterator<Term> iterator = terms.iterator ();

            //循环取出每一个词
            while (iterator.hasNext ()){
                Term term =  iterator.next ();

                //词性的过滤
                if(term.getNatureStr ()==null || term.getNatureStr ().equals ( "w" )){
                    iterator.remove ();
                    continue;
                }

                //词长度的过滤
                if(term.getRealName ().length ()<2){
                    iterator.remove ();
                    continue;
                }

                //统计 == 如果map里有这个词语，在原来基础上加一，否则第一次存入，count=1
                String realname = term.getRealName ();
                Integer count = 0;
                if(map.containsKey ( realname )){
                    count = map.get ( realname )+1;
                }else{
                    count=1;
                }
                map.put ( realname,count );

            }


        }
        List<WordCount> wordCounts = new ArrayList<> (  );
        for(Map.Entry<String,Integer> entry:map.entrySet ()){
            WordCount wordCount = new WordCount ();
            wordCount.setWord ( entry.getKey () );
            wordCount.setCount ( entry.getValue () );
            wordCounts.add ( wordCount );
        }
        return wordCounts;
    }

//    public static void main(String[] args) {
//       Result result = NlpAnalysis.parse ( "九天阊阖开宫殿，万国衣冠拜冕旒。" );
//       List<Term> list = result.getTerms ();
//        for(Term term:list){
//            System.out.println (term);
//        }
//    }
}
