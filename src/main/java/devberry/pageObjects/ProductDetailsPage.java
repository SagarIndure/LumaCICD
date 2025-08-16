package devberry.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import devberry.AbstractComponents.AbstractComponent;

public class ProductDetailsPage extends AbstractComponent{
	
	WebDriver driver;
	public ProductDetailsPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "div[id*='size']")
	List<WebElement> sizes;
	
	@FindBy(css = "div[id*='color']")
	List<WebElement> colors;
	
	@FindBy(css = "#qty")
	WebElement quantity;
	
	@FindBy(css = "#product-addtocart-button")
	WebElement addToCartBtn;
	
	@FindBy(css = "div[data-ui-id=\"message-success\"]")
	WebElement messageContent;
	
	
	By messageElement = By.cssSelector("div[data-ui-id=\"message-success\"]");
	
	
	//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ui-id-28")));

	//driver.findElement(By.cssSelector("#product-addtocart-button")).click();

	// driver.findElement(By.cssSelector("#qty")).sendKeys("2");
	//List<WebElement> sizes = driver.findElements(By.cssSelector("div[id*='size']"));
	//List<WebElement> colors = driver.findElements(By.cssSelector("div[id*='color']"));
	
	public void getDesiredSize(String sizeNeeded) {
		WebElement desiredSize = sizes.stream().filter(size -> size.getText().equals(sizeNeeded)).findFirst().orElse(null);
		if (!desiredSize.isSelected()) {
			desiredSize.click();
		}
	}
	
	
	public void getDesiredColour(String colorText) {
		
		WebElement desiredColor = colors.stream().filter(color -> color.getAttribute("aria-label").equals(colorText))
				.findFirst().orElse(null);

		desiredColor.click();
	}
	
	public void setQuantity(String setQty) {
		quantity.clear();
		quantity.sendKeys(setQty);
	}
	
	public String addToCart() {
		addToCartBtn.click();
		waitForMessage(messageElement);
		String cartMsg = messageContent.getText();
		return cartMsg;
	}
}
