package TPF_API;

import static org.testng.Assert.fail;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;

public class Readcsv {
	

public static List<String> ReadcsvFile(String Direction ,String csvName ,int ...columns) {

		List<String>PNRsStrings = new ArrayList<>();
		File directionFile = new File(Direction);
		File readFile = new File(directionFile,csvName);
		try {
			FileReader Freader = new FileReader(readFile);
			CSVReader BufferedReader = new CSVReader(Freader);
			String[] LineString = BufferedReader.readNext(); 
	
			while ((LineString = BufferedReader.readNext()) != null) {
				for(int i :columns)
				{
					if(i < LineString.length) {
						PNRsStrings.add(LineString[i]);
					}
					else {
						fail("The CSV file '" + csvName + "' does not contain column number " + i +" .");
					}
				}
//				Assert.assertEquals(false, true);
			}
			BufferedReader.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
		return PNRsStrings;
	}
}
