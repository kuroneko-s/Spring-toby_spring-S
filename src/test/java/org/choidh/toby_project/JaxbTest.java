package org.choidh.toby_project;

import org.choidh.toby_project.domain.dao.xjc.SqlType;
import org.choidh.toby_project.domain.dao.xjc.Sqlmap;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JaxbTest {

    @Test
    public void readSqlmap() throws Exception{
        final String contextPath = Sqlmap.class.getPackage().getName();

//        File file = new File("C:\\project\\workspace\\toby_project\\src\\test\\resources\\sqlmap.xml");
//        FileSystemResource fileSystemResource = new FileSystemResource("classpath:/config/sqlmap.xml");
        Path path = Paths.get(File.separatorChar + "config" + File.separatorChar + "sqlmap.xml");
//        InputStream inputStream = getClass().getResourceAsStream(path.toString());

        ClassPathResource resource = new ClassPathResource(path.toString());

        final JAXBContext context = JAXBContext.newInstance(contextPath);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(resource.getInputStream());

        final List<SqlType> sql = sqlmap.getSql();

        assertEquals(sql.size(), 3);
        this.checkList(sql, new String[]{"add", "get", "delete"}, new String[]{"insert", "select", "delete"});
    }

    private void checkList(List<SqlType> list, String[] keys, String[] values) {
        int len = list.size();
        for (int i = 0; i < len; i++) {
            assertEquals(list.get(i).getKey(), keys[i]);
        }
        for (int i = 0; i < len; i++) {
            assertEquals(list.get(i).getValue(), values[i]);
        }
    }
}
