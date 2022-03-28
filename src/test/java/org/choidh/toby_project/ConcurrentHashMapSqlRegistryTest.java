package org.choidh.toby_project;

import org.choidh.toby_project.domain.dao.sql.ConcurrentHashMapSqlRegistry;
import org.choidh.toby_project.domain.dao.sql.UpdateableSqlRegistry;
import org.choidh.toby_project.exception.SqlNotFoundException;
import org.choidh.toby_project.exception.SqlUpdateFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcurrentHashMapSqlRegistryTest {
    UpdateableSqlRegistry sqlRegistry;

    @BeforeEach
    public void setUp() {
        sqlRegistry = new ConcurrentHashMapSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    @Test
    public void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    private void checkFindResult(String sql1, String sql2, String sql3) {
        assertEquals(sqlRegistry.findSql("KEY1"), sql1);
        assertEquals(sqlRegistry.findSql("KEY2"), sql2);
        assertEquals(sqlRegistry.findSql("KEY3"), sql3);
    }

    @Test
    public void unknownKey() {
        assertThrows(SqlNotFoundException.class, () -> {
            sqlRegistry.findSql("SQL9999!@#");
        });
    }

    @Test
    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    @DisplayName("한 번에 여러개의 SQL문을 수정하는 기능을 검증")
    void updateMulti() {
        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("KEY1", "Modified1");
        sqlMap.put("KEY3", "Modified3");

        sqlRegistry.updateSql(sqlMap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test
    @DisplayName("존재하지 않는 키의 SQL문 변경 시도")
    void updateWithNotExistingKey() {
        assertThrows(SqlUpdateFailureException.class, () -> {
            sqlRegistry.updateSql("SQL123!@#", "Modified2");
        });
    }

}
