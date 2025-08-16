package devberry.Tests;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CompleteFlow {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--allow-insecure-localhost");
		options.addArguments("--disable-save-password-bubble");
        options.setExperimentalOption("prefs", Map.of(
            "credentials_enable_service", false,
            "profile.password_manager_enabled", false
        ));
		
		ChromeDriver driver = new ChromeDriver(options);
		
		driver.get("https://magento247-pub.test");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.findElement(By.cssSelector(".authorization-link")).click();
		driver.findElement(By.cssSelector("#email")).sendKeys("sagarindurepatil@gmail.com");
		driver.findElement(By.cssSelector("#password")).sendKeys("Circuit@#32");
		driver.findElement(By.cssSelector("button[class=\"action login primary\"]")).click();
		
		Actions actions = new Actions(driver);
		
		WebElement hotSellersSection = driver.findElement(By.xpath("//h2[text()='Hot Sellers']"));
		
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", hotSellersSection);
		
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {
			
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Hot Sellers']")));
		
		List<WebElement> products = driver.findElements(By.cssSelector(".product-item"));
		
		WebElement desiredItem = products.stream().filter(product -> product.findElement(By.cssSelector(".product-item-name")).getText().equals("Radiant Tee")).findFirst().orElse(null);
		
		actions.moveToElement(desiredItem).perform();
		
		desiredItem.findElement(By.cssSelector(".tocart")).click();
		
		Thread.sleep(2000);
		
		List<WebElement> sizes =  driver.findElements(By.cssSelector("div[id*='size']"));
		
		WebElement desiredSize = sizes.stream().filter(size -> size.getText().equals("S")).findFirst().orElse(null);
		
		System.out.println(desiredSize.getText());
		
		if(!desiredSize.isSelected()) {
			desiredSize.click();
		}
		
		List<WebElement> colors = driver.findElements(By.cssSelector("div[id*='color']"));
		WebElement desiredColor = colors.stream().filter(color -> color.getAttribute("aria-label").equals("Orange")).findFirst().orElse(null);
		
		desiredColor.click();
		
		//driver.findElement(By.cssSelector("#qty")).sendKeys("2");
		driver.findElement(By.cssSelector("#product-addtocart-button")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[data-ui-id=\"message-success\"]")));
		String cartMsg = driver.findElement(By.cssSelector("div[data-ui-id=\"message-success\"]")).getText();
		Assert.assertEquals(cartMsg, "You added Radiant Tee to your shopping cart.");
		
		driver.findElement(By.cssSelector(".showcart")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ui-id-28")));
		driver.findElement(By.cssSelector("#top-cart-btn-checkout")).click();
		
		wait.until(ExpectedConditions.urlToBe("https://magento247-pub.test/checkout/#shipping"));
		
		List<WebElement> addresses = driver.findElements(By.cssSelector(".shipping-address-items div"));
		
		WebElement selectedAddress = addresses.stream().filter(address -> address.getAttribute("class").contains("selected-item")).findFirst().orElse(null);
		System.out.println("Selected Address is : "+selectedAddress.getText());
	
		driver.findElement(By.cssSelector(".items-in-cart")).click();
		String qtyText = driver.findElement(By.cssSelector(".details-qty .value")).getText();
		int qtyInCart = Integer.parseInt(qtyText);
		
		double totalShippingCost = qtyInCart * 5;
		
		String shippingCostText = driver.findElement(By.cssSelector(".price")).getText();
		double displayedShippingCost = Double.parseDouble(shippingCostText.substring(1));
		
		Assert.assertEquals(totalShippingCost, displayedShippingCost);
		
		
		driver.findElement(By.cssSelector("button[class*='continue']")).click();
		
		
		wait.until(ExpectedConditions.urlToBe("https://magento247-pub.test/checkout/#payment"));
		
		List<WebElement> rows = driver.findElements(By.cssSelector(".table-totals tbody tr"));
		
		WebElement subTotal =rows.stream().filter(row -> row.findElement(By.tagName("th")).getText().contains("Cart Subtotal")).findFirst().orElse(null);
		String subTotalValue = subTotal.findElement(By.tagName("td")).getText();
		double subAmount = Double.parseDouble(subTotalValue.substring(1));
		
		WebElement shippingRow = rows.stream().filter(row2 -> row2.findElement(By.tagName("th")).getText().contains("Shipping")).findFirst().orElse(null);
		String shippingValue = shippingRow.findElement(By.tagName("td")).getText();
		double shippingAmount = Double.parseDouble(shippingValue.substring(1));
		double discAmount = 0.00;
		WebElement disc = rows.stream().filter(row3 -> row3.findElement(By.tagName("th")).getText().contains("Discount")).findFirst().orElse(null);
		if(disc!=null) {
			String discValue = disc.findElement(By.tagName("td")).getText();
			discAmount = Double.parseDouble(discValue.substring(2));
		}
		WebElement orderTotal = rows.stream().filter(row4 -> row4.findElement(By.tagName("th")).getText().contains("Order Total")).findFirst().orElse(null);
		String orderValue = orderTotal.findElement(By.tagName("td")).getText();
		double orderAmount = Double.parseDouble(orderValue.substring(1));
		
		
		
		
		double expectedOrderTotal = subAmount + shippingAmount;
		
		expectedOrderTotal = expectedOrderTotal - discAmount;
		
		//System.out.println(expectedOrderTotal);
		
		Assert.assertEquals(expectedOrderTotal, orderAmount);
		
		WebElement placeOrderBtn = driver.findElement(By.cssSelector("button[class=\"action primary checkout\"]"));
		wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", placeOrderBtn);
		
		wait.until(ExpectedConditions.urlToBe("https://magento247-pub.test/checkout/onepage/success/"));
		
		String sucessMsg = driver.findElement(By.cssSelector(".base")).getText();
		Assert.assertEquals("Thank you for your purchase!", sucessMsg);
		
		String orderID = driver.findElement(By.cssSelector(".order-number strong")).getText();
		
		System.out.println("Your Order ID is : "+orderID);
		
		
		System.out.println("Test completed. Press Enter to exit...");
		System.in.read();  // Waits until you hit Enter in terminal
		driver.quit();
	}

}
