package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.exception.ExceptionComent;
import org.choidh.toby_project.exception.SqlNotFoundException;
import org.choidh.toby_project.exception.SqlUpdateFailureException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapSqlRegistry implements UpdateableSqlRegistry {

    private Map<String, String> sqlMap = new ConcurrentHashMap<>();

    @Override
    public void clean() {

    }

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

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        if ( sqlMap.get(key) == null ) {
            throw new SqlUpdateFailureException(ExceptionComent.sqlNotFoundMessage(key));
        }

        sqlMap.put(key, sql);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }
}
