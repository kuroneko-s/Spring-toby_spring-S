package org.choidh.toby_project.domain;

import org.choidh.toby_project.domain.dao.sql.ConcurrentHashMapSqlRegistry;
import org.choidh.toby_project.domain.dao.sql.UpdateableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdateableSqlRegistryTest{
    @Override
    protected UpdateableSqlRegistry createUpdateableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
