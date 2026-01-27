package destination;

import org.testng.Assert;
import utils.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class caxitaDB {
//    @Test
//    void test(){
//        List<String> ffff = new ArrayList<>();
//        ffff.add("FNGZTK");
//        ffff.add("FNG7CB");
//        List<String> ll = GetExcludedPnrs(ffff);
//
//
//        for(int i = 0 ;i<ll.size() ;i++){
//            System.out.println(ll.get(i));
//        }
//    }
    public static List<String > GetExcludedPnrs(List<String> stringList){
        List<String>  Local = new ArrayList<>();
        String PnrsString = stringList.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
        String sql = "SELECT SupplierPnr FROM FlightBookingDetail WHERE SupplierPnr IN (" +PnrsString+ ")";

        dbConnection SQLConnection = new dbConnection("10.2.0.4",1454,"Live_Flywt",
                "client_flywt","Cl!3nt@FwT2025",true);
        Connection ConnectionObject = SQLConnection.Connect();
        try {
            PreparedStatement Pr = ConnectionObject.prepareStatement(sql);
            ResultSet result = Pr.executeQuery();
            while (result.next()){
                Local.add(result.getString("SupplierPnr"));
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
