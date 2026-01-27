package utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.net.ServerSocket;

public class dbConnection {
    private final String Url;
    private final String UserName;
    private final String Password ;

    private  String HostName ;
    private final String SshHostName ;
    private final String SshUserName ;
    private final String SshPassword;
    private final boolean UseSsc;
    private final int freeport;

    private Session session;
    private Connection connection;

    public dbConnection(String MySQl_HostName, String MySQl_dbName , int MySQl_port, String MySQl_UserName, String MySQl_Password){
        // Connect to public MySQL database
        this.freeport = MySQl_port;
        this.Url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC", MySQl_HostName,freeport,MySQl_dbName);
        this.UserName = MySQl_UserName;
        this.Password = MySQl_Password;
        this.SshHostName = "";
        this.SshUserName = "";
        this.SshPassword = "";
        this.UseSsc = false;
    }
    public dbConnection(String MySQl_HostName, String MySQl_dbName , String MySQl_UserName, String MySQl_Password
            , String sshHostName, String sshUserName, String sshPassword ){
        // Connect to private MySQL database
        this.freeport = getFreePort();
        this.Url = String.format("jdbc:mysql://localhost:%d/%s?useSSL=false&serverTimezone=UTC",freeport,MySQl_dbName);
        this.UserName = MySQl_UserName;
        this.Password = MySQl_Password;
        this.HostName = MySQl_HostName;
        this.SshHostName = sshHostName;
        this.SshUserName = sshUserName;
        this.SshPassword = sshPassword;
        this.UseSsc = true;
    }

    public dbConnection(String SQL_serverName, int sql_serverPort,String SQL_dbName,String SQL_UserName ,String SQL_password ,boolean SQL_TrustServerCertificate){
        // Connect to public SQL database
        this.freeport = sql_serverPort;
        this.Url = String.format("jdbc:sqlserver://%s:%d;databaseName=%s;encrypt=true;trustServerCertificate=%s"
                                    , SQL_serverName,freeport,SQL_dbName,SQL_TrustServerCertificate);
        this.UserName = SQL_UserName;
        this.Password = SQL_password;
        this.SshHostName = "";
        this.SshUserName = "";
        this.SshPassword = "";
        this.UseSsc = false;
    }


    public Connection Connect() {
        try {
            if (UseSsc) {
                openTunnel();
            }
            connection = DriverManager.getConnection(Url, UserName, Password);
            return connection;
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null; // caller must check for null
        }
    }


    private  void openTunnel() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SshUserName, SshHostName, 22);
            session.setPassword(SshPassword);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            // localPort, remoteHost, remotePort
            session.setPortForwardingL(freeport, HostName, 3306);

        } catch (Exception e) {
            throw new RuntimeException("SSH tunnel failed", e);
        }
    }

    public  void disConnect() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println("fail to close " +e.getMessage());
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    private int getFreePort() {
        int port = -1; // default if unable to get a port

        try (ServerSocket socket = new ServerSocket(0)) {
            // 0 = let system pick a free port
            port = socket.getLocalPort();
        } catch (Exception e) {
            System.out.println("Failed to get free port: " + e.getMessage());
        }
        return port;
    }
}
