package org.choidh.toby_project.config;

import org.choidh.toby_project.domain.dao.sql.EmbeddedDbSqlRegistry;
import org.choidh.toby_project.domain.dao.sql.OxmSqlService;
import org.choidh.toby_project.domain.dao.sql.SqlRegistry;
import org.choidh.toby_project.domain.dao.sql.SqlService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

@Configuration
public class SqlServiceContext {

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
//        sqlService.setSqlMap(new ClassPathResource("/config/sqlmap.xml"));
        return sqlService;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.choidh.toby_project.domain.dao.xjc");
        return marshaller;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());

        return sqlRegistry;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .build();
    }
}
