package org.choidh.toby_project;

import org.choidh.toby_project.domain.dao.xjc.SqlType;
import org.choidh.toby_project.domain.dao.xjc.Sqlmap;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JaxbTest {

    @Test
    public void readSqlmap() throws Exception{
        final String contextPath = Sqlmap.class.getPackage().getName();
        final JAXBContext context = JAXBContext.newInstance(contextPath);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
                getClass().getResourceAsStream("sqlmap.xml")
        );

        final List<SqlType> sql = sqlmap.getSql();

        assertEquals(sql.size(), 3);
    }
}
