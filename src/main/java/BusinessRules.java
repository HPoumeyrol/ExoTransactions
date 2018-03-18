
public class BusinessRules {
	
	static public void addCreditToCustomer(Customer customer, Cash cash, Double amountCreditoAdd) {
		
		
		System.out.format("\n\tTACHE 1 : Provision de %1$.2f crédits pour %2$s\n",amountCreditoAdd, customer.getName());
		if (DbConnection.setAutoCommit(false)) //  if setting autocomit to false succed
		{
			customer.setCredit(customer.getCredit() + amountCreditoAdd);
			cash.setAmount(cash.getAmount() + amountCreditoAdd);
			if (! DbConnection.commit()) //test if commit is OK
			{
				DbConnection.rollback(); // if not then rollback
			}
			
		}
		else
		{
			System.err.println("Error : transaction not allowed because error occured when setting autocomit");
		}
		
		if (! DbConnection.setAutoCommit(true)) //  if restoring autocomit to true succed
		{
			System.err.println("!!! WARNING !!! Autocommit is not restored to TRUE !!!");
		}
		customer.refresh();
		cash.refresh();
		System.out.format("\tle Nouveau solde de %1$s est %2$.2f crédits\n", customer.getName(), customer.getCredit());
		System.out.format("\tle Nouveau solde de Cash est %1$.2f crédits\n", cash.getAmount());
		System.out.println("");
	}
	

	static public void supplyProductStock(Product product, Integer qtyToAdd) {
		System.out.format("\n\tTACHE 2 : Ajout de %1$3d %2$s au stock\n", qtyToAdd, product.getLabel());
		product.setQty(product.getQty() + qtyToAdd);
		System.out.format("\tLe nouveau stock de %1$s est %2$d\n", product.getLabel(), product.getQty());
		System.out.println("");
	}

	
	static public void buyProduct(Customer customer, Product product, Integer qtyToBuy) {
		System.out.format("\n\tTACHE 3 : %1$s achète %2$d %3$s\n",customer.getName(), qtyToBuy, product.getLabel());
		Double saleTotal= product.getPrice() * qtyToBuy; // Compute saleTotal = Product price * qtyToBuy
		System.out.format("\tAchat : %1$d %2$s à %3$.2f € = %4$.2f €\n",qtyToBuy, product.getLabel(), product.getPrice(), saleTotal);
				
		
		if (DbConnection.setAutoCommit(false)) //  if setting autocomit to false succed
		{
			
			
			//Update product Stock
			System.out.format("\tSortie de %1$d %2$s du stock.\n", qtyToBuy, product.getLabel());
			product.setQty(product.getQty() - qtyToBuy);
			
			//update customer Credit
			System.out.format("\tDébit de %1$.2f € sur le compte de %2$s\n",saleTotal, customer.getName());
			customer.setCredit(customer.getCredit() - saleTotal);
			
			//register Sale
			Sales_log sales_log= Sales_log.create(product.getPk_id(), qtyToBuy);
			sales_log.display();
						
			if (! DbConnection.commit()) //test if commit is OK
			{
				DbConnection.rollback(); // if not then rollback
			}
			
		}
		else
		{
			System.err.println("Error : transaction not allowed because error occured when setting autocomit");
		}
		
		if (! DbConnection.setAutoCommit(true)) //  if restoring autocomit to true succed
		{
			System.err.println("!!! WARNING !!! Autocommit is not restored to TRUE !!!");
		}
		
		
		product.refresh(); System.out.format("\tNouvel etat du stock = %1$d %2$s\n", product.getQty(), product.getLabel());
		customer.refresh();	System.out.format("\tLe nouveau solde du compte de %1$s est %2$.2f €\n", customer.getName(), customer.getCredit());
		
		System.out.println("");
		
		
		
		
		
		
				
		
	}
	

	
}
