package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.domain.dao.xjc.SqlType;
import org.choidh.toby_project.domain.dao.xjc.Sqlmap;
import org.choidh.toby_project.exception.SqlNotFoundException;
import org.choidh.toby_project.exception.SqlRetrievalFailureException;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService, SqlReader, SqlRegistry{
    Map<String, String> sqlMap = new HashMap<>();
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;
    private String filePath;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    // SqlReader
    @Override
    public void read(SqlRegistry registry) {
        // File.separatorChar + "config" + File.separatorChar + "sqlmap.xml"
        Path path = Paths.get(this.filePath);
        ClassPathResource resource = new ClassPathResource(path.toString());
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class.getPackageName()); // XML 문서를 오브젝트 트리로 읽어온다.
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(resource.getInputStream());

            for (SqlType type: sqlmap.getSql()) {
                this.sqlRegistry.registerSql(type.getKey(), type.getValue());
            }
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // SqlRegistry
    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    // SqlRegistry
    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String query = sqlMap.get(key);
        if (query == null) {
            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        }else {
            return query;
        }
    }

    // SqlService
    @Override
    public String getSql(String key) throws SqlNotFoundException {
        try {
            return this.sqlRegistry.findSql(key);
        }catch (SqlNotFoundException e) {
            throw new SqlNotFoundException("notfound", e);
        }
    }
}
