package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author Chandrashekhar Gomasa
 * @project TestNGWithMavenFramework
 */

public class OracleDbConnection {

    static ResourceBundle config = ResourceBundle.getBundle("configuration.Settings");

    public static Connection getCMSConnection() {

        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //String url = "jdbc:oracle:thin:@172.16.13.240:1521:NECBMS";
            // String url = "jdbc:oracle:thin:@10.10.7.31:1521:NECBMS"; //Box-6 Data Base connection Settings
            //connection = DriverManager.getConnection(url, "CMS_TESTING", "CMS_TESTING");//SIT
            //connection= DriverManager.getConnection(url,"NWREPORTS","NWREPORTS");//DIT
            //connection = DriverManager.getConnection(url, "DITCMS_TESTING", "DITCMS_TESTING");// BOX-6 SIT

            String url = "jdbc:oracle:thin:@" + config.getString("cmsHostName") + "" + config.getString("cmsPort") + "" + config.getString("cmsServiceName") + "";
            connection = DriverManager.getConnection(url, config.getString("cmsUserName"), config.getString("cmsPassword"));

            PreparedStatement ps = connection.prepareStatement("Select * from Table");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));

            connection.close();

        } catch (ClassNotFoundException cne) {
            cne.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Connection getBMSConnection() {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //String url = "jdbc:oracle:thin:@172.16.13.240:1521:NECBMS";
            // String url = "jdbc:oracle:thin:@10.10.7.31:1521:NECBMS"; //Box-6 Data Base connection Settings
            //connection = DriverManager.getConnection(url, "CMS_TESTING", "CMS_TESTING");//SIT
            //connection= DriverManager.getConnection(url,"NWREPORTS","NWREPORTS");//DIT
            //connection = DriverManager.getConnection(url, "DITCMS_TESTING", "DITCMS_TESTING");// BOX-6 SIT

            String url = "jdbc:oracle:thin:@" + config.getString("bmsHostName") + "" + config.getString("bmsPort") + "" + config.getString("bmsServiceName") + "";
            connection = DriverManager.getConnection(url, config.getString("bmsUserName"), config.getString("bmsPassword"));

            PreparedStatement ps = connection.prepareStatement("Select * from Table");
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));

            connection.close();

        } catch (ClassNotFoundException cne) {
            cne.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


}