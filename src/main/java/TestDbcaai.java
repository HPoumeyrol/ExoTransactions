import java.util.Random;

public class TestDbcaai {
	static final int NB_RUNS_TO_DO= 10;
	
	public static void main(String args[]) {
		Boolean doTruncate= false;
		Cash cash1= null; 
		Customer customer1= null;
		Product product1= null;
		Vendor vendor1= null;
		
		for(String arg : args) {
			if(arg.toUpperCase().equals("DOTRUNCATE")) doTruncate= true;
		}
		
		
		Params.display();
		
		
		
		if(DbConnection.isDbConnected()) {
			
			if(doTruncate) {
				System.out.println("Truncate demandé !");
				Sale_log.truncate(); //Vider sales_log
				Cash.truncate(); cash1= Cash.create(0.0); //Creer un cash
				Customer.truncate(); customer1= Customer.create("Pierre DURAND",0.0); //Creer un client;
				Vendor.truncate(); 
				Vendor.create("Vendeur 1", false); //Creer un vendeur;
				Vendor.create("Vendeur 2", false); //Creer un vendeur;
				Vendor.create("Vendeur 3", false); //Creer un vendeur;
				Vendor.create("Vendeur 4", false); //Creer un vendeur;
				Vendor.create("Vendeur 5", false); //Creer un vendeur;
				Product.truncate(); product1= Product.create("PC Lenovo", 15.0, 0); //Creer un produit
			}
			else
			{
				cash1= Cash.getCashFromDb();
				customer1= Customer.findCustomerByName("Pierre DURAND");
				product1= Product.findProductByLabel("PC Lenovo");
			}
			vendor1= Vendor.findFreeVendor();
			vendor1.setIs_Busy(true); //Réserve le vendeur;
					
			
			
			System.out.println("");
			System.out.println("Start");
			
						
			// Test loop
			Random rng = new Random();
			for(int run=0; run != NB_RUNS_TO_DO; ++run) {
				System.out.format("\n\n-------------- run %1$2d ---------------\n", run);
				cash1.refresh();
				customer1.refresh();
				product1.refresh();
				vendor1.refresh();
				
				System.out.format("Current Database values :\n\t%1$s\n\t%2$s\n\t%3$s\n\t%4$s\n", cash1,customer1,product1,vendor1);
				
				// Create random values
				Double creditCustomerToAdd=(rng.nextInt(100)+1)* 1.0;
				Integer qtyProductToAdd = rng.nextInt(10)+1;
				Integer qtyProductToBuy = rng.nextInt(10)+1;
				
				
				System.out.format("\n\t TACHES A REALISER : Provision de %1$.2f crédits, Ajout au stock de %3$d %2$s, Achat de %4$d %2$s\n", creditCustomerToAdd, product1.getLabel(), qtyProductToAdd, qtyProductToBuy);
				BusinessRules.addCreditToCustomer(customer1, cash1, creditCustomerToAdd);
				
				BusinessRules.supplyProductStock(product1, qtyProductToAdd);
				BusinessRules.buyProduct(vendor1, customer1, product1, qtyProductToBuy);
				System.out.format("-------------- end run %1$2d ---------------\n", run);
				
			}
			
			System.out.println("");
			
			vendor1.setIs_Busy(false); //libère le vendeur
			vendor1.refresh();
			
			cash1.refresh();
			customer1.refresh();
			product1.refresh();
			System.out.format("Current Database values :\n\t%1$s\n\t%2$s\n\t%3$s\n\t%4$s\n", cash1,customer1,product1,vendor1);
			Sale_log.printAllSalesLog();
			Double salesTotal= Sale_log.getTotalSalesAmount();
			System.out.format("Le total des ventes est de %1$.2f €\n", salesTotal);
			if(cash1.getAmount() == customer1.getCredit() + salesTotal) {
				System.out.println("La balance des ventes est OK");
			} else {
				System.out.println("La balance des ventes est erronée.");
			}
				
		}

	}

}
