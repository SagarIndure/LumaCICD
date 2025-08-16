package devberry.pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import devberry.AbstractComponents.AbstractComponent;

public class LandingPage extends AbstractComponent{
	
	WebDriver driver;
	
	public LandingPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(css = ".product-item")
	List<WebElement> products;
	
	By itemName = By.cssSelector(".product-item-name");
	By addToCartBtn = By.cssSelector(".tocart");
	
	
	public WebElement getDesiredProduct(String desiredItemName) {
        return products.stream().filter(product -> product.findElement(itemName).getText().contains(desiredItemName))
                .findFirst().orElse(null);
	}
	
	public ProductDetailsPage addItemToCart(String itemName) throws InterruptedException {
		WebElement desiredItem = getDesiredProduct(itemName);
		moveToElement(desiredItem);
		desiredItem.findElement(addToCartBtn).click();
        return new ProductDetailsPage(driver);
	}
	
	public void goTo() {
		driver.get("https://magento247-pub.test");
	}
	
	

	
}
