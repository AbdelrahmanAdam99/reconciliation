package TPF_API;

import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;
import static org.testng.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.spi.InetAddressResolver;
import java.util.ArrayList;
import java.util.List;

//import org.checkerframework.dataflow.qual.TerminatesExecution;
import org.testng.annotations.Test;

public class test {
	@Test 
	void  ReadPortalDB () {
//		 Galileo ffGalileo = new Galileo();
		 
		 
		 List<String> fffStrings = Readcsv.ReadcsvFile("D:\\reconcilation mini\\19.12","ndc.csv" , 0,1);

		 String[] ffStrings =  fffStrings.toArray(new String[0]);
		 System.out.println(ffStrings.length);
		 for(int i = 0 ; i< ffStrings.length ; i++)
		 {
			System.out.println(ffStrings[i]);
		 }
//		List<String> galiloActivePnrStrings = new ArrayList<>();
//		File Read = new File(configuration.DIRECTIONSR_STRING);
//		File read = new File(Read,configuration.GALILEO_FILE_NAME_STRING);
//		
//		try {
//			FileReader reader = new FileReader(read);
//			BufferedReader csvReader = new BufferedReader(reader);
//			String line=  csvReader.readLine();
//			String[] linesplit = line.split(",");
//			if(linesplit[0].equalsIgnoreCase("pnr") )
//			{
//				fail(configuration.GALILEO_FILE_NAME_STRING + " is Empty");
//			}
//			
//			
//			while((line =  csvReader.readLine()) != null){
//				linesplit = line.split(",");
//				galiloActivePnrStrings.add(linesplit[0]);
//			}
//			String[] ddStrings = galiloActivePnrStrings.toArray(new String[0]);
//				
//			
//			for (int i= 0 ; i <ddStrings.length ; i++  )
//					System.out.println(ddStrings[i]);
//				{
//					
//				}
//			
//				csvReader.close();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			fail();
//		}
//		return (galiloActivePnrStrings.toArray(new String[0]));
	}

}
