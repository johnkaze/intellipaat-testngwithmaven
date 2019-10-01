package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Chandrashekhar Gomasa
 * @project TestNGWithMavenFramework
 */

public class SqlServerConnection {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // the sql server driver string
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");

            // the sql server url
            String url = "jdbc:microsoft:sqlserver://HOST:1433;DatabaseName=DATABASE";

            // get the sql server database connection
            connection = DriverManager.getConnection(url, "THE_USER", "THE_PASSWORD");

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select * from emp");

            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));

            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

}
