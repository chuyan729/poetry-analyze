package com.chuyan.tangshianalyze.crawler.common;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储清洗的数据
 * Author： chuyan
 * Date：2019/6/12
 */
@ToString
public class DataSet {
    /**
     * data把DOM解析，清洗之后存储的数据
     * 比如：
     * 标题：xx        （k-v）
     * 作者：xxx       （k-v）
     * 正文：xxxxxxx   （k-v）
     */
    private Map<String,Object> data = new HashMap<> (  );

    public void putData(String key,Object value){
        this.data.put ( key, value );
    }

    public Object getData(String key) {
        return data.get ( key );
    }

    public Map<String, Object> getData() {
        return new HashMap<> ( this.data );
    }
}
