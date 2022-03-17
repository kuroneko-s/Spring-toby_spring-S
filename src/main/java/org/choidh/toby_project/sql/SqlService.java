package org.choidh.toby_project.sql;

import org.choidh.toby_project.exception.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
