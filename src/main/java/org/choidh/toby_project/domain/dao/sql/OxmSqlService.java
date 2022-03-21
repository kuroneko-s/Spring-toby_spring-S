package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.domain.dao.xjc.SqlType;
import org.choidh.toby_project.domain.dao.xjc.Sqlmap;
import org.choidh.toby_project.exception.SqlNotFoundException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OxmSqlService implements SqlService{

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setFilePath(String filePath) {
        this.oxmSqlReader.setFilePath(filePath);
    }

    @PostConstruct
    public void loadSql() {
        this.oxmSqlReader.read(this.sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlNotFoundException {
        try {
            return this.sqlRegistry.findSql(key);
        }catch (SqlNotFoundException e) {
            throw new SqlNotFoundException("notfound", e);
        }
    }

    private final class OxmSqlReader implements SqlReader{
        private final String DEFAULT_FILE_PATH = "/config/sqlmap.xml";
        private Unmarshaller unmarshaller;
        private String filePath = DEFAULT_FILE_PATH;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void read(SqlRegistry registry) {
            Path path = Paths.get(this.filePath);
            ClassPathResource resource = new ClassPathResource(path.toString());
            try {
                Source source = new StreamSource(resource.getInputStream());

                for (SqlType type: sqlmap.getSql()) {
                    sqlRegistry.registerSql(type.getKey(), type.getValue());
                }
            } catch (JAXBException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
