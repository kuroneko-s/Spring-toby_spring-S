package org.choidh.toby_project;

import org.choidh.toby_project.config.TestConfig;
import org.choidh.toby_project.domain.Level;
import org.choidh.toby_project.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EmbeddedDbTest extends TestConfig {
    EmbeddedDatabase db;
    JdbcTemplate template;

    @BeforeEach
    void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/schema.sql")
                .build();

        template = new JdbcTemplate(db);
    }

    @AfterEach
    void tearDown() {
        db.shutdown();
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(rs.getString("id")
            , rs.getString("name"), rs.getString("password")
            , Level.valueOf(rs.getInt("level")), rs.getInt("login")
            , rs.getInt("recommend"), rs.getString("email"));

    @Test
    void initData() {
        assertEquals(template.queryForObject("select count(*) from sqlmap", int.class), 2);

        List<Map<String, String>> result = template.query("select * from sqlmap order by key_", (rs, rowNum) -> {
            Map<String, String> innerResult = new HashMap<>();
            innerResult.put(rs.getString("KEY_"), rs.getString("SQL_"));
            return innerResult;
        });

        assertEquals(result.get(0).get("KEY1"), "SQL1");
        assertEquals(result.get(1).get("KEY2"), "SQL2");
        assertEquals(result.size(), 2);
    }

    @Test
    void insertData() {
        template.update("insert into sqlmap(key_, sql_) values(?, ?)", "KEY3", "SQL3");

        assertEquals(template.queryForObject("select count(*) from sqlmap", int.class), 3);
    }

}
