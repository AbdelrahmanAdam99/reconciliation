package TPF_API;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;
public class missmatch {
	@Test
	public void missmatchtest() {
		List<String> DBList = new ArrayList<>();
		List<String> SupplierList = new ArrayList<>();
		
		List<String> galileoList = Readcsv.ReadcsvFile("D:\\reconcilation mini\\19.12", "galileo1.csv", 0);
		SupplierList.addAll(galileoList);
		
		List<String> NDCList = Readcsv.ReadcsvFile("D:\\reconcilation mini\\19.12", "ndc.csv", 0,1);
		List<String>WTList = Readcsv.ReadcsvFile("D:\\reconcilation mini\\19.12", "wt.csv", 0,1);
		DBList.addAll(NDCList);
		DBList.addAll(WTList);
		
		
		
		List<String> reconciliationList = Readcsv.ReadcsvFile("D:\\reconcilation mini\\19.12", "GalileoReconciliation2025-12-19.csv", 0,1);
		List<String> reconciliationListMissingList = new ArrayList<>();
		List<String> reconciliationListmissmatchList = new ArrayList<>();
		
		
		for(int i = 1 ; i < reconciliationList.size(); i+=2)
		{
			if(reconciliationList.get(i).isBlank())
			{
				reconciliationListMissingList.add(reconciliationList.get(i-1));
			}else 
			{
				reconciliationListmissmatchList.add(reconciliationList.get(i-1));
			}
		}
		
	
		List<String> SupplierNotDB = new ArrayList<>(SupplierList);
		SupplierNotDB.removeAll(DBList);
		
		
		List<String> supplierNOTRecociliation = new ArrayList<>(SupplierNotDB);
		supplierNOTRecociliation.removeAll(reconciliationListMissingList);
		
		
		List<String> RecociliationNOTsupplier = new ArrayList<>(reconciliationListMissingList);
		RecociliationNOTsupplier.removeAll(SupplierNotDB);
		
//		
		System.out.print("PNRs active at suppliers but missing from the database and the reconciliation sheet:");
		PrintList(supplierNOTRecociliation);
		System.out.print("PNRs on the reconciliation sheet that are found in the database and active at the supplier:");
		PrintList(RecociliationNOTsupplier);

//		if(!SupplierNotDB.isEmpty()) 
//			{
//			System.out.print("Number of PNRs found for suppliers but not present in the database:");
//			PrintList(SupplierNotDB);
//			fail();
//			}
//			else {
//				System.out.println("All PNRs for suppliers were found in the database.");
//			}
		
//		List<String> DBNotSupplier = new ArrayList<>(DBList);
//		SupplierNotDB.removeAll(SupplierList);
//		System.out.print("Database NOT Supplier: ");
//		PrintList(DBNotSupplier);
//		
		
	}
	private void PrintList(List<String> List)
	{
		 String[] Strings =  List.toArray(new String[0]);
		 System.out.println(Strings.length);
		 for(int i = 0 ; i< Strings.length ; i++)
		 {
			System.out.println(Strings[i]);
		 }
	}
	
}

