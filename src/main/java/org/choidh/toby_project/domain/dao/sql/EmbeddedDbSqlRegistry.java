package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.exception.ExceptionComent;
import org.choidh.toby_project.exception.SqlNotFoundException;
import org.choidh.toby_project.exception.SqlUpdateFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdateableSqlRegistry{
    JdbcTemplate template;
    TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource)
        );
    }

    public void clean() {
        this.template.execute("delete from sqlmap");
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int result = this.template.update("update sqlmap SET sql_ = ? where key_ = ?", sql, key);
        if (result <= 0) {
            throw new SqlUpdateFailureException(ExceptionComent.sqlNotFoundMessage(key));
        }
    }

    @Override
    public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
        /*transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                sqlmap.entrySet().forEach(entry -> updateSql(entry.getKey(), entry.getValue()));
            }
        });*/

        transactionTemplate.execute(status -> {
            sqlmap.entrySet().forEach(entry -> updateSql(entry.getKey(), entry.getValue()));
            return null;
        });
    }

    @Override
    public void registerSql(String key, String sql) {
        this.template.update("insert into sqlmap(key_, sql_) values(?, ?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return this.template.queryForObject("select sql_ from sqlmap where key_ = ?", String.class, key);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(ExceptionComent.sqlNotFoundMessage(key), e);
        }
    }
}
