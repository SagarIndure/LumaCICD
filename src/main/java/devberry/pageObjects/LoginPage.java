package devberry.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import devberry.AbstractComponents.AbstractComponent;

public class LoginPage extends AbstractComponent{
	
	WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		
	}
	
	
	
	@FindBy(id = "email")
	WebElement emailLocator;

	@FindBy(id = "password")
	WebElement passwordLocator;
	
	@FindBy(css = "button[class=\"action login primary\"]")
	WebElement loginButtonLocator;
	
	@FindBy(css = ".message-error div")
	WebElement loginErrorMessage;
	
	
	public void loginApplication(String email, String password) {
		emailLocator.sendKeys(email);
		passwordLocator.sendKeys(password);
		loginButtonLocator.click();
	}
	
	public String getErrorMessage() {
		String errorMessage = loginErrorMessage.getText();
		return errorMessage;
	}
}
