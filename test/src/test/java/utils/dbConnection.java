package utils;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {

    private final String Url;
    private final String UserName;
    private final String Password;
    private final String SshHostName;
    private final String SshUserName;
    private final String SshPassword;
    private final boolean UseSsc;


    private  Session session;


    public dbConnection(String HostName,int Port,String dbName ,String UserName, String Password ){
        this.Url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC",HostName,Port,dbName);
        this.UserName = UserName;
        this.Password = Password;
        this.UseSsc = false;
    }
    public dbConnection(String sshHostName,String sshUserName,String sshPassword
            , String SshHostName, String SshUserName ,String SshPassword
            ,String HostName,int Port,String dbName ,String UserName, String Password){
        this.Url = dbUrl;
        this.UserName = dbUserName;
        this.Password = dbPassword;
        this.SshHostName = SshHostName;
        this.SshUserName = SshUserName;
        this.SshPassword = SshPassword;
        this.UseSsc = true;
    }



    public Connection getConnection() throws Exception{
        if(UseSsc){
            openTunnel();
        }
        return DriverManager.getConnection(Url,UserName,Password);
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
            session.setPortForwardingL(3307, "10.20.0.6", 3306);

        } catch (Exception e) {
            throw new RuntimeException("SSH tunnel failed", e);
        }
    }

//    public static void closeTunnel() {
//        if (session != null && session.isConnected()) {
//            session.disconnect();
//        }
//    }
}
