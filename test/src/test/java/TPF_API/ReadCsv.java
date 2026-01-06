package TPF_API;

import static org.testng.Assert.fail;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import org.testng.Assert;
import org.testng.annotations.Test;
public class ReadCsv {


//public void test(){
//	List<Map<String,String>> PNRsStrings = ReadCsvFile("C:\\Users\\Lenovo\\Desktop\\New folder\\26.11" ,"WT db.csv");
//        for (Map<String, String> pnRsString : PNRsStrings) {
//            System.out.println(pnRsString.get("SP_PNR"));
//        }
//}

public static List<Map<String,String>> ReadCsvFile(String Direction ,String csvName ) {

		List<Map<String,String>> PNRsStrings = new ArrayList<>();
		File directionFile = new File(Direction);
		File readFile = new File(directionFile,csvName);
		try {
			FileReader FileReader = new FileReader(readFile);
			CSVReader BufferedReader = new CSVReader(FileReader);
			String[] HeaderLine = BufferedReader.readNext();
            Assert.assertTrue(HeaderLine.length > 1, "The file " +csvName +" is empty.");
			String[] LineString;

			while ((LineString = BufferedReader.readNext()) != null) {
				HashMap<String,String> Local = new HashMap<>();
				for(int i =0 ; i <  HeaderLine.length ;i++)
				{
					Local.put(HeaderLine[i],LineString[i]);
				}
				PNRsStrings.add(Local);
			}

			BufferedReader.close();
		}catch (Exception e) {
			System.out.println(e.getMessage() + "as");
			fail();
		}

	return PNRsStrings;
	}
}
