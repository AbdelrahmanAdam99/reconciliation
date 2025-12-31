package TPF_API;

import static io.restassured.RestAssured.*;
//import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.testng.Assert.fail;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import io.restassured.response.Response;

public class Galileo {
	private final String Base_URL = "https://tbf-api-prod.azurewebsites.net";
	private final String tokenString = "7J2kV1q0b3y6R4M9s8tZ2p0qL1u3vG5hK9m4c8rF0xW2b5nC6y7s8v9t0q1w2e3r4t5y6u7i==";
	private final String STATUS= "A";
	private final String PAGE_Size= "100";
	private final String[] White_List = {"3HB3","6TY7","6Z9H","7K1J","880J","8JY1","8QV0","D40","WV5","XI7"};		

	
public List<String> exportGalileoDataToCsv() {
		List<String> galiloActivePnrStrings = new ArrayList<>();
		Response response_Body = retrievePageNumber("1");
		if (response_Body.statusCode() != 200) {
			System.out.println(response_Body.getBody().asString());
			fail();
		}
		
		JSONObject pagination = new JSONObject(response_Body.getHeader("X-Pagination"));	
        int TotalPages = pagination.getInt("TotalPages");

		try {
			File Direction = new File(configuration.DIRECTIONSR_STRING);
			File file = new File(Direction,configuration.GALILEO_FILE_NAME_STRING);
			FileWriter csvWriter = new FileWriter(file);
	        csvWriter.append("pnr,createdDate,status,pcc,ticketingAgentSignOn\n");
	        galiloActivePnrStrings = fileWrite(csvWriter , TotalPages); 
	        csvWriter.flush();
	        csvWriter.close();
			}

		 catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
		return galiloActivePnrStrings;
	}


	
	
private List<String> fileWrite(FileWriter file , int totalPage) throws Exception {
	
	   List<String> galiloActivePnrStrings = new ArrayList<>();
		for(int i= 1 ; i <= totalPage; i++)
		{		
		  Response responseBody = retrievePageNumber(Integer.toString(i));
		  JSONArray pageArray = new JSONArray(responseBody.getBody().asString());
		
		  for (int j = 0; j <  pageArray.length(); j++)
		  		{	
			  	JSONObject record = pageArray.getJSONObject(j);
//			  	String ttString = record.toString();	
//			  	System.out.println(record);
//			  	String[] ddStrings = ttString.split(",");
//			  	for(int s = 0 ; s < ddStrings.length ; s++)
//			  	{
//			  		System.out.println(ddStrings[s]);
//			  	}
//			  	Assert.assertEquals(false, true);
			 
			  	for(int n = 0 ; n < White_List.length ; n++)
			  			{
			  				if( White_List[n].equals(record.getString("pcc").trim()) )
			  					{
//			  						System.out.println(record.getString("pnr"));
			  						galiloActivePnrStrings.add(record.getString("pnr"));
//			  						Assert.assertEquals(false, true);

			  						file.append(record.getString("pnr")).append(",");
			  						file.append(record.getString("createdDate")).append(",");
			  						file.append(record.getString("status")).append(",");
			  						file.append(record.getString("pcc").trim()).append(",");
			  						file.append(record.getString("ticketingAgentSignOn").trim()).append("\n");
//	    							Assert.assertEquals(false, true);
			  					}
			  			}
				
				
		  		}
		}
		return galiloActivePnrStrings;
}
	
	

	
private Response  retrievePageNumber(String PageNumber) {
		HashMap<String, String>  reqBody = new HashMap<>();
		
		reqBody.put("startDate", configuration.STAR_Date);
		reqBody.put("endDate", configuration.END_Date);
		reqBody.put("PageNumber", PageNumber);
		reqBody.put("PageSize", PAGE_Size);
		reqBody.put("Status", STATUS);
		
		Response response_Body = given()
				.header("content-type", "application/json")
				.header("x-api-key",tokenString)
				.body(reqBody)
		.when()
				.post(Base_URL + "/api/TBFBooking/RetriveAvailableTBFBooking");	

	return response_Body;
	}
	
}
