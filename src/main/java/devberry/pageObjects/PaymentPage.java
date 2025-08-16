package devberry.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import devberry.AbstractComponents.AbstractComponent;

public class PaymentPage extends AbstractComponent{
	
	WebDriver driver;
	public PaymentPage(WebDriver driver) {
		
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	
	@FindBy(css = ".table-totals tbody tr")
	List<WebElement> rows;
	
	@FindBy(css = "button[class=\"action primary checkout\"]")
	WebElement placeOrderBtn;
	
	By rowTitle = By.tagName("th");
	By rowAmount = By.tagName("td");
	By placeBtnByLocator = By.cssSelector("button[class='action primary checkout']");

	public double getSubtotalAmount() {
		
		WebElement subTotal = rows.stream()
				.filter(row -> row.findElement(rowTitle).getText().contains("Cart Subtotal")).findFirst()
				.orElse(null);
		String subTotalValue = subTotal.findElement(rowAmount).getText();
		double subAmount = Double.parseDouble(subTotalValue.substring(1));
		
		return subAmount;
	}
	
	public double getShippingAmount() {
		WebElement shippingRow = rows.stream()
				.filter(row2 -> row2.findElement(rowTitle).getText().contains("Shipping")).findFirst()
				.orElse(null);
		String shippingValue = shippingRow.findElement(rowAmount).getText();
		double shippingAmount = Double.parseDouble(shippingValue.substring(1));
		
		return shippingAmount;
	}
	
	public double getDiscountAmount() {
		double discAmount = 0.00;
		WebElement disc = rows.stream()
				.filter(row3 -> row3.findElement(rowTitle).getText().contains("Discount")).findFirst()
				.orElse(null);
		if (disc != null) {
			String discValue = disc.findElement(rowAmount).getText();
			discAmount = Double.parseDouble(discValue.substring(2));
		}
		return discAmount;
	}
	
	public double calculateOrderTotal(double subTotalAmount, double shippingAmount) {
		double orderTotal = subTotalAmount + shippingAmount;
		return orderTotal;
		
	}
	
	public double calculateDiscount(double calculatedTotal, double discountAmount) {
		
		double amountAfterDiscount = calculatedTotal - discountAmount;
		return amountAfterDiscount;
	}
	
	public double getTotalSystemAmount() {
		WebElement orderTotal = rows.stream().filter(row4 -> row4.findElement(rowTitle).getText().contains("Order Total")).findFirst().orElse(null);
		String orderValue = orderTotal.findElement(rowAmount).getText();
		double orderAmountBySystem = Double.parseDouble(orderValue.substring(1));
		return orderAmountBySystem;
	}
	
	public OrderConfirmationPage placeOrder() {
		
		waitTillClickable(placeBtnByLocator);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderBtn);
		waitForPageLoad("https://magento247-pub.test/checkout/onepage/success/");
		
		OrderConfirmationPage orderConfirmationPage = new OrderConfirmationPage(driver);
		return orderConfirmationPage;
	}

}
