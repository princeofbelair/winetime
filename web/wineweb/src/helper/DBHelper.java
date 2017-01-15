package helper;

import java.sql.Connection;
import java.sql.DriverManager;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

/**
 * Database Helper for MYSQL and SPARQL Connections
 */
public class DBHelper {

    private static String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/Winetime";
    private static String NADINE_MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/winetime";
    private static String MYSQL_USER = "root";
    private static String MYSQL_PW = "root";

    public static Connection mySQLConnection;

    static {

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(NADINE_MYSQL_URL);
        dataSource.setUser(MYSQL_USER);
        dataSource.setPassword(MYSQL_PW);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            DBHelper.mySQLConnection = dataSource.getConnection();
            //mysqlConnection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PW);
            System.out.println("Connected to MYSQL DB");
        } catch(Exception ex) {
            System.out.println("Could not establish mysql connection. Maybe the MySQL server is offline.");
            System.out.println(ex.getMessage());
        }

    }



}
