package org.choidh.toby_project.domain.dao.sql;

import org.choidh.toby_project.domain.dao.xjc.SqlType;
import org.choidh.toby_project.domain.dao.xjc.Sqlmap;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JaxbXmlSqlReader implements SqlReader{
    private final String DEFAULT_FILE_PATH = "/config/sqlmap.xml";

    private String filePath = DEFAULT_FILE_PATH;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void read(SqlRegistry registry) {
        Path path = Paths.get(this.filePath);
        ClassPathResource resource = new ClassPathResource(path.toString());
        try {
            JAXBContext context = JAXBContext.newInstance(Sqlmap.class.getPackageName()); // XML 문서를 오브젝트 트리로 읽어온다.
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(resource.getInputStream());

            for (SqlType type: sqlmap.getSql()) {
                registry.registerSql(type.getKey(), type.getValue());
            }
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
