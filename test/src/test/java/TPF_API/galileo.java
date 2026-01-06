package TPF_API;

import static io.restassured.RestAssured.*;
//import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.testng.Assert.fail;

import java.util.*;
import java.io.File;
import java.io.FileWriter;

import com.opencsv.CSVWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class galileo{
	private static final String BASE_URL = "https://tbf-api-prod.azurewebsites.net";
	private static final String API_KEY = "7J2kV1q0b3y6R4M9s8tZ2p0qL1u3vG5hK9m4c8rF0xW2b5nC6y7s8v9t0q1w2e3r4t5y6u7i==";
	private static final String STATUS= "A";
	private static final String PAGE_Size= "100";
	private static final String[] CSV_HEADERS = {"pnr", "createdDate", "status", "pcc", "ticketingAgentSignOn"};
	private static final Set<String> PCC_WHITE_LIST = Set.of("3HB3","6TY7","6Z9H","7K1J","880J","8JY1","8QV0","D40","WV5","XI7");

//	@Test
//public void test(){
//		List<Map<String,String>> PNRsStrings = exportGalileoDataToCsv();
//		for(int i = 0; i <PNRsStrings.size() ;i++){
//			System.out.println(PNRsStrings.get(i).get("pnr"));
//		}
//}

public List<Map<String,String>> exportGalileoDataToCsv() {
	List<Map<String,String>> GaliloActivePnrs = new ArrayList<>();

		Response response_Body = RetrievePageNumber("1");
		Assert.assertEquals(response_Body.statusCode() ,200,response_Body.getBody().asString());

		JSONObject pagination = new JSONObject(response_Body.getHeader("X-Pagination"));
        int TotalPages = pagination.getInt("TotalPages");

		try {
			File Direction = new File(configuration.DIRECTIONSR_STRING);
			File Csvfile = new File(Direction,configuration.GALILEO_FILE_NAME_STRING);
			CSVWriter CsvWriter = new CSVWriter(new FileWriter(Csvfile));

			GaliloActivePnrs = fileWrite(CsvWriter, TotalPages);
	        CsvWriter.flush();
	        CsvWriter.close();
			}

		 catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
		return GaliloActivePnrs;
	}



	public List<Map<String, String>> fileWrite(CSVWriter file , int totalPage) throws Exception {

		List<Map<String, String>> GaliloActivepnrs = new ArrayList<>();
		file.writeNext(CSV_HEADERS);
		for (int i = 1; i <= totalPage; i++) {
			Response responseBody = RetrievePageNumber(Integer.toString(i));
			JSONArray pageArray = new JSONArray(responseBody.getBody().asString());
			
			for (int j = 0; j < pageArray.length(); j++) {
				JSONObject RecordJson = pageArray.getJSONObject(j);
				Map<String,String> RecordMap = FromJsonObjectToMap(RecordJson);
					String PCC = RecordJson.getString("pcc").trim().toUpperCase();
					if (PCC_WHITE_LIST.contains(PCC)) {
						List<String > WriteString = new ArrayList<>();
						for (int s = 0 ; s< RecordMap.size()  ; s++)
						{
								WriteString.add(RecordMap.get(CSV_HEADERS[s]));
						}
						file.writeNext(WriteString.toArray(new String[0]));
						GaliloActivepnrs.add(RecordMap);
					}
			}
		}
	return GaliloActivepnrs;
}

	
private Map<String,String> FromJsonObjectToMap(JSONObject JsonObject){
	Map<String,String> Local = new LinkedHashMap<>();
    for (String s : CSV_HEADERS) {
        Local.put(s, JsonObject.getString(s));
    }
	return Local;
}

	
private Response  RetrievePageNumber(String PageNumber) {
		HashMap<String, String>  reqBody = new HashMap<>();
		reqBody.put("startDate", configuration.STAR_Date);
		reqBody.put("endDate", configuration.END_Date);
		reqBody.put("PageNumber", PageNumber);
		reqBody.put("PageSize", PAGE_Size);
		reqBody.put("Status", STATUS);

		Response response_Body = given()
				.header("content-type", "application/json")
				.header("x-api-key", API_KEY)
				.body(reqBody)
		.when()
				.post(BASE_URL + "/api/TBFBooking/RetriveAvailableTBFBooking");

	return response_Body;
	}
	
}
