package com.chuyan.tangshianalyze.crawler.pipeline;

import com.chuyan.tangshianalyze.crawler.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 将Page数据写入到数据库
 *
 * Author： chuyan
 * Date：2019/6/15
 */
public class DataBasePipeline implements Pipeline {

    private final Logger logger = LoggerFactory.getLogger ( DataBasePipeline.class );

    private final DataSource dataSource;

    public DataBasePipeline(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void pipeline(final Page page) {

        String title = (String) page.getDataSet ().getData ("title");
        String dynasty = (String) page.getDataSet ().getData ( "dynasty" );
        String author = (String) page.getDataSet ().getData ( "author" );
        String content = (String) page.getDataSet ().getData ( "content" );

        String sql = "insert into poetry_info(title, dynasty, author, content) values(?,?,?,?)";

        try(Connection connection = dataSource.getConnection ();
            PreparedStatement statement = connection.prepareStatement ( sql )
        ){
            statement.setString ( 1,title );
            statement.setString ( 2,dynasty );
            statement.setString ( 3,author );
            statement.setString ( 4,content );

            statement.executeUpdate ();

        }catch (SQLException e){
           // e.printStackTrace ();
            logger.error ( "DataBase insert occur excrption {} .",e.getMessage () );
        }

        /*
        PoetryInfo poetryInfo = (PoetryInfo) page.getDataSet ().getData ("poetry");

        String sql = "insert into poetry_info(title, dynasty, author, content) values(?,?,?,?)";


        try (Connection connection = dataSource.getConnection ();
             PreparedStatement statement = connection.prepareStatement ( sql )
        ){
            statement.setString(1,poetryInfo.getTitle ());
            statement.setString ( 2,poetryInfo.getDynasty () );
            statement.setString ( 3,poetryInfo.getAuthor () );
            statement.setString ( 4,poetryInfo.getContent () );

            statement.executeUpdate ();

        } catch (SQLException e) {
            e.printStackTrace ();
        }
        */
    }
}
