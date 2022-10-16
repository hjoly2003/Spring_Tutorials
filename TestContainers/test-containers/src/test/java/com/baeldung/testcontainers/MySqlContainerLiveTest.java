package com.baeldung.testcontainers;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;
import org.testcontainers.containers.MySQLContainer;

@Testable
public class MySqlContainerLiveTest {
    @Rule
    public MySQLContainer mysqlContainer = new MySQLContainer();

    @Test
    public void whenSelectQueryExecuted_thenResulstsReturned() throws Exception {
        ResultSet resultSet = performQuery(mysqlContainer, "SELECT 1");
        resultSet.next();
        int result = resultSet.getInt(1);
        assertEquals(1, result);
    }

    private ResultSet performQuery(MySQLContainer mysql, String query) throws SQLException {
        String jdbcUrl = mysql.getJdbcUrl();
        String username = mysql.getUsername();
        String password = mysql.getPassword();
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        return conn.createStatement()
            .executeQuery(query);
    }
}
