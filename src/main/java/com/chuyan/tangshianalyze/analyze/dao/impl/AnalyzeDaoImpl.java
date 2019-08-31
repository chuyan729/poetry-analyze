package com.chuyan.tangshianalyze.analyze.dao.impl;

import com.chuyan.tangshianalyze.analyze.dao.AnalyzeDao;
import com.chuyan.tangshianalyze.analyze.entity.PoetryInfo;
import com.chuyan.tangshianalyze.analyze.model.AuthorCount;
import com.sun.scenario.effect.impl.prism.PrImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author： chuyan
 * Date：2019/6/16
 */
public class AnalyzeDaoImpl implements AnalyzeDao {

    private final Logger logger = LoggerFactory.getLogger ( AnalyzeDao.class );
    private final DataSource dataSource;

    public AnalyzeDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {

        List<AuthorCount> datas = new ArrayList<> (  );

        String sql = " select count(*) as count,author from poetry_info group by author;";

        //try() 自动关闭
        try(Connection connection = dataSource.getConnection ();
            PreparedStatement statement = connection.prepareStatement ( sql );
            ResultSet resultSet = statement.executeQuery ()
        ){
            while (resultSet.next ()){
                AuthorCount authorCount = new AuthorCount ();
                authorCount.setAuthor ( resultSet.getString ( "author" ) );
                authorCount.setCount ( resultSet.getInt ( "count" ) );

                datas.add(authorCount);
            }

        }catch (SQLException e){
           // e.printStackTrace ();
            logger.error ( "DtaBase query occur exception {} .",e.getMessage () );
        }

        return datas;
    }

    @Override
    public List<PoetryInfo> queryPortryInfo() {

        List<PoetryInfo> datas = new ArrayList<> (  );
        String sql = "select title,dynasty,author,content from poetry_info;";

        try(Connection connection = dataSource.getConnection ();
            PreparedStatement statement = connection.prepareStatement ( sql );
            ResultSet resultSet = statement.executeQuery ()
        ){
            while (resultSet.next ()){
                PoetryInfo poetryInfo = new PoetryInfo ();
                poetryInfo.setTitle ( resultSet.getString ( "title" ) );
                poetryInfo.setDynasty ( resultSet.getString ( "dynasty" ) );
                poetryInfo.setAuthor ( resultSet.getString ( "author" ) );
                poetryInfo.setContent ( resultSet.getString ( "content" ) );

                datas.add ( poetryInfo );
            }
        } catch (SQLException e) {
            //e.printStackTrace ();
            logger.error("Database query occur exception {}.",e.getMessage());
        }

        return datas;
    }
}
