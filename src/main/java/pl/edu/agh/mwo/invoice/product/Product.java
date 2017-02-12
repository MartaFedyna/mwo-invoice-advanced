package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
	private final String name;

	private final BigDecimal price;

	private final BigDecimal taxPercent;

	protected Product(String name, BigDecimal price, BigDecimal tax) {
		if(name!=null && name!=""){
			this.name = name;
		} else {
			throw new IllegalArgumentException();
		}
		
		if (price != null && price.compareTo(BigDecimal.ZERO)>0){
			this.price = price;
		} else {
			throw new IllegalArgumentException();
		}
		this.taxPercent = tax;
	}

	public String getName() {
		return this.name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public BigDecimal getTaxPercent() {
		return this.taxPercent;
	}

	public BigDecimal getPriceWithTax() {
		return (new BigDecimal(1).add(this.taxPercent).multiply(this.price));
	}
}
