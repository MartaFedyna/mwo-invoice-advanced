package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private HashMap<Product, Integer> products=new HashMap<Product, Integer>();
	private int invoiceNumber;
	private static int nextNumber=1; //static - nie należy do instancji obiektu więc jest przechowywany niezale znie od instancji
	
	public Invoice(){
		this.invoiceNumber=nextNumber;
		nextNumber+=1;
	}

	public static void ResetNextNumber(){
		nextNumber=1;
	}
	
	public void addProduct(Product product) {
		if(products.containsKey(product)){
			Integer oldQuantity=products.get(product);
			products.put(product, oldQuantity+1);
		} else {
			products.put(product, 1);
		}
	}

	public void addProduct(Product product, Integer quantity) {
		if(quantity<=0){
			throw new IllegalArgumentException();
		}
		if(products.containsKey(product)){
			Integer oldQuantity=products.get(product);
			products.put(product, oldQuantity+quantity);
		} else {
			products.put(product, quantity);
		}
	}

	public BigDecimal getSubtotal() {
		if(products.isEmpty()){
			return BigDecimal.ZERO;
		} else {
			BigDecimal subTot = BigDecimal.ZERO;
			for(Product product: products.keySet()){
				BigDecimal position=product.getPrice().multiply(new BigDecimal(products.get(product)));
				subTot=subTot.add(position);
			}
			return subTot;
		}
	}

	public BigDecimal getTax() {
		if(products.isEmpty()){
			return BigDecimal.ZERO;
		} else {
			BigDecimal tax = BigDecimal.ZERO;
			for(Product product: products.keySet()){
				BigDecimal position=product.getTaxPercent().multiply(product.getPrice()).multiply(new BigDecimal(products.get(product)));
				tax=tax.add(position);
			}
			return tax;
		}
	}

	public BigDecimal getTotal() {
		if(products.isEmpty()){
			return BigDecimal.ZERO;
		} else {
			BigDecimal total = BigDecimal.ZERO;
			for(Product product: products.keySet()){
				BigDecimal position=product.getTaxPercent().add(new BigDecimal(1)).multiply(product.getPrice()).multiply(new BigDecimal(products.get(product)));
				total=total.add(position);
			}
			return total;
		}
	}

	public Integer getNumber() {
		return invoiceNumber;
	}
}
