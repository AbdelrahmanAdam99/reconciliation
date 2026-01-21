package destination;

import org.testng.annotations.Test;
import utils.dbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import utils.writeCsvUtil;


public class wtDB {
    private static final String wtDbURl = "jdbc:mysql://localhost:3307/wonderdb?useSSL=false&serverTimezone=UTC";
    private static final String wtDbUsername = "readuser";
    private static final String wtDbPassword = "anj-tho-bee";
    private static final String wtDbStartData = "2026-01-13 00:00:00";
    private static final String wtDbEndData = "2026-01-20 00:00:00";


    private static final String wtsqlQuery =
            "SELECT DISTINCT SD.SP_PNR, SD.AIRLINE_PNR, SD.BOOKING_DATE, FB.BOOKING_STATUS " +
                    "FROM wonderdb.TT_TS_FB_SEGMENT_DETAIL SD " +
                    "JOIN wonderdb.TT_TS_FLIGHT_BOOK FB " +
                    "ON FB.FB_BOOKING_REF_NO = SD.FB_BOOKING_REF_NO " +
                    "WHERE FB.CREATION_TIME BETWEEN ? AND ?";


    public static  List<Map<String, String>> exportWTData(String Direction, String csvName) {
        dbConnection.openTunnel();
        List<Map<String, String>> localList = new ArrayList<>();

        try {
//            Connection conn = dbConnection.getDBConnection(wtDbURl, wtDbUsername, wtDbPassword);
            PreparedStatement ps = conn.prepareStatement(wtsqlQuery, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ps.setFetchSize(Integer.MIN_VALUE);
            ps.setString(1, wtDbStartData);
            ps.setString(2, wtDbEndData);
            ResultSet resp = ps.executeQuery();

            while (resp.next()) {
                Map<String, String> LocalMap = new LinkedHashMap<>();
                LocalMap.put("SP_PNR", getAsIs(resp,"SP_PNR"));
                LocalMap.put("AIRLINE_PNR", getAsIs(resp,"AIRLINE_PNR"));
                LocalMap.put("BOOKING_DATE", getAsIs(resp,"BOOKING_DATE"));
                LocalMap.put("BOOKING_STATUS", getAsIs(resp,"BOOKING_STATUS"));
                localList.add(LocalMap);
            }
            resp.close();
            ps.close();
            conn.close();
            writeCsvUtil.WriteCsvFile(localList, Direction, csvName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally {
            dbConnection.closeTunnel();
        }

        return localList;
    }
    private static String getAsIs(ResultSet rs, String column) throws Exception {
        Object value = rs.getObject(column);

        if (value == null && rs.wasNull()) {
            return "NULL";   // REAL SQL NULL
        }
        return value.toString(); // empty string stays empty, value stays value
    }
    @Test
    public void test() {
        List<Map<String, String>> kk = exportWTData("D:\\\\reconcilation mini\\\\19.01", "WT12.csv");

        for (Map<String, String> pnRsString : kk) {
            System.out.println("SP_PNR: " + pnRsString.get("SP_PNR")
                    + " | AIRLINE_PNR: " + pnRsString.get("AIRLINE_PNR")
                    + " | BOOKING_DATE: " + pnRsString.get("BOOKING_DATE")
                    + " | BOOKING_STATUS: " + pnRsString.get("BOOKING_STATUS"
            ));
        }

    }
}