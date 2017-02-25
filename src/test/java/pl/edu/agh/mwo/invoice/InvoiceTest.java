package pl.edu.agh.mwo.invoice;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
	private static final String PRODUCT_1 = "Product 1";
	private static final String PRODUCT_2 = "Product 2";
	private static final String PRODUCT_3 = "Product 3";

	@Before
	public void resetInvoiceNumber(){
		Invoice.ResetNextNumber();
	}
	
	@Test
	public void testEmptyInvoiceHasEmptySubtotal() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getSubtotal());
	}

	@Test
	public void testEmptyInvoiceHasEmptyTaxAmount() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getTax());
	}

	@Test
	public void testEmptyInvoiceHasEmptyTotal() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getTotal());
	}

	@Test
	public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		assertBigDecimalsAreEqual(invoice.getSubtotal(), invoice.getTotal());
	}

	@Test
	public void testInvoiceHasProperSubtotalForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		invoice.addProduct(createOtherProduct(), 1);
		invoice.addProduct(createDairyProduct(), 1);
		assertBigDecimalsAreEqual("259.99", invoice.getSubtotal());
	}

	@Test
	public void testInvoiceHasProperTaxValueForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		invoice.addProduct(createOtherProduct(), 1);
		invoice.addProduct(createDairyProduct(), 1);
		assertBigDecimalsAreEqual("12.3", invoice.getTax());
	}

	@Test
	public void testInvoiceHasProperTotalValueForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct());
		invoice.addProduct(createOtherProduct());
		invoice.addProduct(createDairyProduct());
		assertBigDecimalsAreEqual("272.29", invoice.getTotal());
	}

	@Test
	public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 3); // Subtotal: 599.97
		invoice.addProduct(createOtherProduct(), 2); // Subtotal: 100.00
		invoice.addProduct(createDairyProduct(), 4); // Subtotal: 40.00
		assertBigDecimalsAreEqual("739.97", invoice.getSubtotal());
	}

	@Test
	public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 3); // Total: 599.97
		invoice.addProduct(createOtherProduct(), 2); // Total: 123.00
		invoice.addProduct(createDairyProduct(), 4); // Total: 43.2
		assertBigDecimalsAreEqual("766.17", invoice.getTotal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithZeroQuantity() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithNegativeQuantity() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), -1);
	}

	/* Ponizej test standardowy
	@Test
	public void testInvoiceHasNumberGreaterThanZero(){
		Invoice invoice = createEmptyInvoice();
		Assert.assertTrue(invoice.getNumber()>0);
	}
	*/
	
	// W metodzie ponizej korzystamy z Hamcrest
	@Test
	public void testInvoiceHasNumberGreaterThanZero(){
		Invoice invoice = createEmptyInvoice();
		Assert.assertThat(invoice.getNumber(), Matchers.greaterThan(0));
	}
	
	@Test
	public void testIfTwoInvoicesHasDifferentNumbers(){
		Invoice invoice = createEmptyInvoice();
		Invoice invoice1 = createEmptyInvoice();
		Assert.assertNotEquals(invoice.getNumber(), invoice1.getNumber());
	}
	
	@Test
	public void testInvoiceAlwaysHasTheSameNumber(){
		Invoice invoice = createEmptyInvoice();
		Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
	}
	
	@Test
	public void testInvoiceAlwaysHasDifferentNumber(){
		Invoice invoice = createEmptyInvoice();
		Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
	}
	
	@Test
	public void testPrintedInvoiceHasNumber(){
		Invoice invoice = createEmptyInvoice();
		int number = invoice.getNumber();
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice, Matchers.containsString(String.valueOf(number)));
	}
	
	@Test
	public void testPrintedInvoiceHasProductName(){
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal("100")));
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice, Matchers.containsString("Mleko"));
	}
	
	@Test
	public void testPrintedInvoiceHasProductType(){
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal("100")));
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice, Matchers.containsString("DairyProduct"));
	}
	
	@Test
	public void testPrintedInvoiceHasProducQuantity(){
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal("100")),1827);
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice, Matchers.containsString("1827"));
	}
	
	@Test
	public void testPrintedInvoiceSize(){
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal("100")),1827);
		invoice.addProduct(new DairyProduct("Mleko2", new BigDecimal("100")),1827);
		invoice.addProduct(new DairyProduct("Mleko3", new BigDecimal("100")),1827);
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice, Matchers.containsString("Liczba pozycji: 3"));
	}
	
	@Test
	public void testPrintedInvoiceWithOnlyOneProductAddedTwice(){
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal("100")),1827);
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal("100")),1827);
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice, Matchers.containsString("Liczba pozycji: 1"));
	}
	
	private Invoice createEmptyInvoice() {
		return new Invoice();
	}

	private Product createTaxFreeProduct() {
		return new TaxFreeProduct(PRODUCT_1, new BigDecimal("199.99"));
	}

	private Product createOtherProduct() {
		return new OtherProduct(PRODUCT_2, new BigDecimal("50.0"));
	}

	private Product createDairyProduct() {
		return new DairyProduct(PRODUCT_3, new BigDecimal("10.0"));
	}

	private void assertBigDecimalsAreEqual(String expected, BigDecimal actual) {
		assertEquals(new BigDecimal(expected).stripTrailingZeros(), actual.stripTrailingZeros());
	}

	private void assertBigDecimalsAreEqual(BigDecimal expected, BigDecimal actual) {
		assertEquals(expected.stripTrailingZeros(), actual.stripTrailingZeros());
	}
	
	

}
