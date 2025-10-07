package com.puzzlesolverappbackend.puzzlesolverapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none", // don't validate in this smoke test
        "logging.level.liquibase=info"
})
class LiquibaseSmokeTest {

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void liquibaseCreatedTables() {
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE() AND table_name = 'akari'", Integer.class);
        assertThat(cnt).isNotNull();
        assertThat(cnt).isGreaterThan(0); // akari table exists
    }
}
