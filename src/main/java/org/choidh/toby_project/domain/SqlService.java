package org.choidh.toby_project.domain;

import org.choidh.toby_project.handler.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
