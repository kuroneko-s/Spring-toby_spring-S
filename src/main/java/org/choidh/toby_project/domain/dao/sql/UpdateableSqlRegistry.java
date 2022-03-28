package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.exception.SqlUpdateFailureException;

import java.util.Map;

public interface UpdateableSqlRegistry extends SqlRegistry{

    public void updateSql(String key, String sql) throws SqlUpdateFailureException;

    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
