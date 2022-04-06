package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.exception.SqlNotFoundException;

import javax.annotation.PostConstruct;

public class AdminSqlService implements SqlService{

    protected SqlReader sqlReader = new JaxbXmlSqlReader();
    protected UpdateableSqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(UpdateableSqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlNotFoundException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlNotFoundException(e);
        }
    }

}
