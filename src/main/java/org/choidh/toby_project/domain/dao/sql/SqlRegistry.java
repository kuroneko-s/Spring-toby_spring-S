package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.exception.SqlNotFoundException;

public interface SqlRegistry {
    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException;
}
