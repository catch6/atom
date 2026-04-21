package cn.mindit.atom.test.mybatisplus;

import javax.sql.DataSource;
import java.sql.SQLException;

public final class TestDDLHelper {

    private TestDDLHelper() {
    }

    public static boolean isPostgresql(DataSource dataSource) {
        try (var connection = dataSource.getConnection()) {
            return connection.getMetaData().getURL().startsWith("jdbc:postgresql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createTestUserTable(DataSource dataSource) {
        if (isPostgresql(dataSource)) {
            return "CREATE TABLE IF NOT EXISTS test_user (" +
                    "id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                    "name VARCHAR(64)," +
                    "age INT," +
                    "create_time TIMESTAMP," +
                    "update_time TIMESTAMP," +
                    "delete_time TIMESTAMP DEFAULT NULL" +
                    ")";
        }
        return "CREATE TABLE IF NOT EXISTS test_user (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(64)," +
                "age INT," +
                "create_time DATETIME," +
                "update_time DATETIME," +
                "delete_time DATETIME DEFAULT NULL" +
                ")";
    }

}
