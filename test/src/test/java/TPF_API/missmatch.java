package TPF_API;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
public class missmatch {
	@Test
	public void missmatchtest() {
		List<String> DBList = new ArrayList<>();
		List<String> SupplierList = new ArrayList<>();

		List<Map<String,String>> galileoList = ReadCsv.ReadCsvFile("D:\\\\reconcilation mini\\\\19.12", "galileo1.csv");
		for (Map<String, String> stringStringMap : galileoList) {
			SupplierList.add(stringStringMap.get("pnr"));
		}


        List<Map<String,String>> NDCList = ReadCsv.ReadCsvFile("D:\\\\reconcilation mini\\\\19.12", "ndc.csv");
        for (Map<String, String> stringStringMap : NDCList) {
            DBList.add(stringStringMap.get("SP_PNR"));
            DBList.add(stringStringMap.get("AIRLINE_PNR"));
        }

		List<Map<String,String>> WTList = ReadCsv.ReadCsvFile("D:\\\\reconcilation mini\\\\19.12", "wt.csv");
		for (Map<String, String> stringStringMap : WTList) {
			DBList.add(stringStringMap.get("SP_PNR"));
			DBList.add(stringStringMap.get("AIRLINE_PNR"));
		}


//

//		for(int i = 0 ; i < SupplierList.size() ; i++)
//		{
//			System.out.println(SupplierList.get(i));
//		}

		List<Map<String,String>> reconciliationSheet = ReadCsv.ReadCsvFile("D:\\\\reconcilation mini\\\\19.12", "GalileoReconciliation2025-12-19.csv");

		List<String> reconciliationListMissingList = new ArrayList<>();
		List<String> reconciliationListmissmatchList = new ArrayList<>();


		for(int i = 0 ; i < reconciliationSheet.size(); i++)
		{
			if(reconciliationSheet.get(i).get("SourceSystem").isEmpty())
			{
				reconciliationListMissingList.add(reconciliationSheet.get(i).get("PNR"));
			}else
			{
				reconciliationListmissmatchList.add(reconciliationSheet.get(i).get("PNR"));
			}
		}

		List<String> SupplierNotDB = new ArrayList<>(SupplierList);
		SupplierNotDB.removeAll(DBList);
//
//
		List<String> supplierNOTRecociliation = new ArrayList<>(SupplierNotDB);
		supplierNOTRecociliation.removeAll(reconciliationListMissingList);
//
//
		List<String> RecociliationNOTsupplier = new ArrayList<>(reconciliationListMissingList);
		RecociliationNOTsupplier.removeAll(SupplierNotDB);
//
////

//
		if(!SupplierNotDB.isEmpty())
			{
			System.out.print("Number of PNRs found for suppliers but not present in the database:");
			PrintList(SupplierNotDB);
			}
			else {
				System.out.println("All PNRs for suppliers were found in the database.");
			}
//
//		List<String> DBNotSupplier = new ArrayList<>(DBList);
//		SupplierNotDB.removeAll(SupplierList);
//		System.out.print("Database NOT Supplier: ");
//		PrintList(DBNotSupplier);



		System.out.print("PNRs active at suppliers but missing from the database and the reconciliation sheet:");
		PrintList(supplierNOTRecociliation);
		System.out.print("PNRs on the reconciliation sheet that are found in the database and active at the supplier:");
		PrintList(RecociliationNOTsupplier);

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

