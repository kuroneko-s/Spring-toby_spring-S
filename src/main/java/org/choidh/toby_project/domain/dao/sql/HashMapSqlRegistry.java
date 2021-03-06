package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.exception.ExceptionComent;
import org.choidh.toby_project.exception.SqlNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry{
    private Map<String, String> sqlMap = new HashMap<>();

    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = this.sqlMap.get(key);
        if ( sql == null ) throw new SqlNotFoundException(ExceptionComent.sqlNotFoundMessage(key));

        return sql;
    }
}
