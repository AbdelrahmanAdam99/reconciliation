package destination;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.dbConnection;
import com.jcraft.jsch.jce.BlowfishCBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class genxDB {

//@Test
//    void test(){
//        List<String> ffff = new ArrayList<>();
//        ffff.add("ECIEYQ");
//        List<String> ll = GetExcludedPnrs(ffff);
//
//
//        for(int i = 0 ;i<ll.size() ;i++){
//            System.out.println(ll.get(i));
//        }
//    }

    public static List<String > GetExcludedPnrs( List<String> stringList){
        List<String>  Local = new ArrayList<>();
        String PnrsString = stringList.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
        String sql = "SELECT GDSPnr FROM Service_FlightBookings WHERE GDSPnr IN (" +PnrsString+ ")";

        dbConnection SQLConnection = new dbConnection("20.234.56.71",1433,"E4TMaster",
                "GXRA_user","GWTX@123!@TraV",true);
        Connection ConnectionObject = SQLConnection.Connect();
        try {
            PreparedStatement Pr = ConnectionObject.prepareStatement(sql);
            ResultSet result = Pr.executeQuery();
            while (result.next()){
                Local.add(result.getString("GDSPnr"));
            }
        }
        catch ( Exception E){
            System.out.println(E.getMessage());
            Assert.fail();
        }
        finally {
            SQLConnection.disConnect();
        }

        return Local;
    }

}
