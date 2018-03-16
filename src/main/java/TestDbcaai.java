import java.sql.SQLException;
import java.util.Random;

public class TestDbcaai {

	public static void main(String args[]) {
		
		Params.getInstance().display();
		
		
		
		if(DbConnection.isDbConnected()) {
			
			//Creer un client;
			Customer.truncate();
			Customer customer1 = Customer.create("Hugues Poumeyrol",0.0);
			customer1.refresh();
			customer1.display();
			
			//Creer un produit
			Product.truncate();
			Product product1 = Product.create(15.0, 0);
			product1.refresh();
			product1.display();

			//Creer un cash
			Cash.truncate();
			Cash cash1 = Cash.create(0.0);
			cash1.refresh();
			cash1.display();
			
			System.out.println("");
			System.out.println("Start");
			
			Random rng = new Random();
			int nbRunToDo= 30;
			for(int run=0; run != nbRunToDo; ++run) {
				System.out.format("\n\n-------------- run %1$2d ---------------\n", run);
				Double creditCustomerToAdd=(rng.nextInt(100)+1)* 1.0;
				Integer qtyProductToAdd = rng.nextInt(10)+1;
				Integer qtyProductToBuy = rng.nextInt(10)+1;
				
				System.out.format("Run %1$2d : Achat de %2$5.2f crédits, Ajout de %3$3d produits, Achat de %4$3d produits", run, creditCustomerToAdd, qtyProductToAdd, qtyProductToBuy);
				try {
					BusinessRules.addCreditToCustomer(customer1, cash1, creditCustomerToAdd);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BusinessRules.supplyProductStock(product1, qtyProductToAdd);
				BusinessRules.buyProduct(customer1, product1, qtyProductToBuy);
				System.out.format("-------------- end run %1$2d ---------------\n", run);
				
			}
			
		}

	}

}
