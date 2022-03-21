package org.choidh.toby_project.domain.dao.sql;

// Default Object
public class DefaultSqlService extends BaseSqlService{
    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
