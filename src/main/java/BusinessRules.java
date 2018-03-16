
public class BusinessRules {
	
	static public void addCreditToCustomer(Customer customer, Cash cash, Double amountCreditoAdd) {
		
		System.out.format("\n\tAchat de %1$5.2f crédits pour %2$s\n",amountCreditoAdd, customer.getName());
		customer.setCredit(customer.getCredit() + amountCreditoAdd);
		cash.setAmount(cash.getAmount() + amountCreditoAdd);
		customer.refresh();
		cash.refresh();
		System.out.format("\tle Nouveau solde de %1$s %2$5.2f crédits\n", customer.getName(), customer.getCredit());
		System.out.format("\tle Nouveau solde de Cash est %1$5.2f crédits\n", cash.getAmount());
		
	}
	

	static public void supplyProductStock(Product product, Integer qtyToAdd) {
		System.out.format("\n\tAjout de %1$3d produits au stock\n", qtyToAdd);
		product.setQty(product.getQty() + qtyToAdd);
		System.out.format("\tLe nouveau stock de produit est %1$3d produits\n", product.getQty());
	}

	
	static public void buyProduct(Customer customer, Product product, Integer qtyToBuy) {
		System.out.format("\n\t%1$s Achète %2$3d produits\n",customer.getName(), qtyToBuy);
		
		// Compute saleTotal = Product price * qtyToBuy
		Double saleTotal= product.getPrice() * qtyToBuy;
		System.out.format("\tAchat :  %1$3d produits à  %2$5.2f € =  %3$5.2f\n",qtyToBuy, product.getPrice(), saleTotal);
		
		//Update product Stock
		System.out.format("\tSortie de  %1$3d produits du stock.\n", qtyToBuy);
		product.setQty(product.getQty() - qtyToBuy);
		product.refresh();
		System.out.format("\tNouvel etat du stock = %1$3d produits\n", product.getQty());
		
		//update customer Credit
		System.out.format("\tDebit de %1$5.2f € sur le compte de %2$s\n",saleTotal, customer.getName());
		customer.setCredit(customer.getCredit() - saleTotal);
		customer.refresh();
		System.out.format("\tLe nouveau solde du compte de %1$s est  %2$5.2f €\n", customer.getName(), customer.getCredit());
		
		//register Sale
		Sales_log sales_log= Sales_log.create(product.getPk_id(), qtyToBuy);
		sales_log.display();
		
	}
	

	
}
