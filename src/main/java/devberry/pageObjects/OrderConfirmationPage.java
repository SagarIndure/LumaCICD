package devberry.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import devberry.AbstractComponents.AbstractComponent;

public class OrderConfirmationPage extends AbstractComponent{

	WebDriver driver;
	public OrderConfirmationPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		
	}
	
	@FindBy(css = ".base")
	WebElement confirmMessage; 
	
	@FindBy(css = ".order-number strong")
	WebElement orderId;
	
	//		String orderID = driver.findElement(By.cssSelector(".order-number strong")).getText();
	//String sucessMsg = driver.findElement(By.cssSelector(".base")).getText();
	
	
	public String getConfirmationMessage() {
		return confirmMessage.getText();
	}
	
	public String getOrderID() {
		return orderId.getText();
	}
}
