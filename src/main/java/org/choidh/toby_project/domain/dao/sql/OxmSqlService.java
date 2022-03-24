package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.domain.dao.xjc.SqlType;
import org.choidh.toby_project.domain.dao.xjc.Sqlmap;
import org.choidh.toby_project.exception.SqlNotFoundException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService{

    private final BaseSqlService baseSqlService = new BaseSqlService();

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlMap(Resource sqlMap) {
        this.oxmSqlReader.setSqlMap(sqlMap);
    }

    @PostConstruct
    public void loadSql() {
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);
        this.baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlNotFoundException {
        return this.baseSqlService.getSql(key);
    }

    private class OxmSqlReader implements SqlReader{
        private Unmarshaller unmarshaller;
        private Resource sqlMap = new ClassPathResource("/config/sqlmap.xml");

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlMap(Resource sqlMap) {
            this.sqlMap = sqlMap;
        }

        @Override
        public void read(SqlRegistry registry) {
            try {
                Source source = new StreamSource(sqlMap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);

                for (SqlType type: sqlmap.getSql()) {
                    sqlRegistry.registerSql(type.getKey(), type.getValue());
                }
            } catch (IOException e) {
                throw new RuntimeException(this.sqlMap.getFilename() + "을 가져올 수 없습니다.", e);
            }
        }
    }
}
