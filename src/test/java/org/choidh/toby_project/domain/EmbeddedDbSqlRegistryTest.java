package org.choidh.toby_project.domain;

import lombok.extern.slf4j.Slf4j;
import org.choidh.toby_project.domain.dao.sql.EmbeddedDbSqlRegistry;
import org.choidh.toby_project.domain.dao.sql.UpdateableSqlRegistry;
import org.choidh.toby_project.exception.SqlUpdateFailureException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
class EmbeddedDbSqlRegistryTest extends AbstractUpdateableSqlRegistryTest{
    EmbeddedDatabase database;

    @Override
    protected UpdateableSqlRegistry createUpdateableSqlRegistry() {
        this.database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/schema.sql")
                .build();
        EmbeddedDbSqlRegistry registry = new EmbeddedDbSqlRegistry();
        registry.setDataSource(database);

        return registry;
    }

    @AfterEach
    public void destroy() {
        this.database.shutdown();
    }

    @Test
    void transactionalUpdate() {
        // H2 에선 트랜잭션 적용이 안되서 동작안할거임
        // @Transactional로 먹인건 H2에선 rollbalck 넣어주는 코드가 안들어가져서 안됬는데
        // TrnasactionTemplate 써서 밀어넣으니간 적용이 됨
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("KEY1", "Modified1");
        sqlMap.put("KEY328173!@#", "Modified321312"); // Error  Code (Key Not Found)

        try{
            this.sqlRegistry.updateSql(sqlMap);
            fail();
        }catch(SqlUpdateFailureException e){
            log.error("Error! " + e.getMessage());
        }
        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}