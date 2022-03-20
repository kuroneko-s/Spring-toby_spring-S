package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.exception.SqlNotFoundException;

public interface SqlService {
    String getSql(String key) throws SqlNotFoundException;
}
