package helper;

import java.sql.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Database Helper for MYSQL and SPARQL Connections
 */
public class DBHelper {

    private static String MYSQL_URL = "jdbc:mysql://localhost:3306/winetime";
    private static String MYSQL_USER = "root";
    private static String MYSQL_PW = "root";

    public DBHelper() {}

    public Connection createMySQLConnection() {

        Connection mysqlConnection = null;
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(MYSQL_URL);
        dataSource.setUser(MYSQL_USER);
        dataSource.setPassword(MYSQL_PW);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            mysqlConnection = dataSource.getConnection();
        } catch(Exception ex) {
            System.out.println("Could not establish mysql connection. Maybe the MySQL server is offline.");
        }

        return mysqlConnection;
    }

}
