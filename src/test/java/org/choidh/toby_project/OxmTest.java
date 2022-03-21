package org.choidh.toby_project;

import org.choidh.toby_project.domain.dao.xjc.SqlType;
import org.choidh.toby_project.domain.dao.xjc.Sqlmap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OxmTest extends TestConfig{
    @Autowired
    Unmarshaller unmarshaller;

    @Test
    void unmarshallerSqlMap() throws Exception {
        Path path = Paths.get(File.separatorChar + "config" + File.separatorChar + "sqlmap.xml");
        ClassPathResource resource = new ClassPathResource(path.toString());
        Source source = new StreamSource(resource.getInputStream());

        Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);

        List<SqlType> sqlTypes = sqlmap.getSql();

        assertEquals(sqlTypes.size(), 6);
    }

}
