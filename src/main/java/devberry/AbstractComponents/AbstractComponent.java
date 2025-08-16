package devberry.AbstractComponents;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import devberry.pageObjects.LoginPage;
import devberry.pageObjects.ShippingAddressPage;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AbstractComponent {

	WebDriver driver;
	WebDriverWait wait;
	

	public AbstractComponent(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(this.driver, Duration.ofSeconds(5));
	}

	@FindBy(css = ".authorization-link")
	WebElement headLoginBtn;

	@FindBy(xpath = "//h2[text()='Hot Sellers']")
	WebElement hotSellersSection;
	
	@FindBy(css = ".showcart")
	WebElement showCartBtn;
	
	@FindBy(css = "#top-cart-btn-checkout")
	WebElement checkoutBtnShowcart;
	
	@FindBy(css =".minicart-wrapper")
	WebElement miniCartBox;

	@FindBy(css = "header[class='page-header'] li:nth-child(3) a")
	WebElement createAccountBtn;

	@FindBy(css = ".not-logged-in")
	WebElement welcomeMessageText;

	@FindBy(tagName = "a")
	List<WebElement> linkURL;

	@FindBy(css = "#search")
	WebElement searchBox;

	@FindBy(css = ".product-item")
	List<WebElement> productItems;

	@FindBy(css = ".toolbar-number")
	WebElement searchProductsCount;

	@FindBy(css = "ol[id='mini-cart'] li")
			List<WebElement> miniCartItems;

    @FindBy(css = ".navigation li a")
       List<WebElement> navigationLinks;

	By sectionLocator =  By.xpath("//h2[text()='Hot Sellers']");
	By itemNameLocator = By.cssSelector(".product-item-link");
	By miniCartItemNameLocator = By.cssSelector(".product-item-name");
	
	public LoginPage getLoginPage() {
		headLoginBtn.click();
        return new LoginPage(driver);
	}

	public void goToSection() {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", hotSellersSection);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

		}
		waitForAction();
	}


	/** Waiting Area **/



	public void waitForAction() {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(sectionLocator));
	}
	
	
	public void moveToElement(WebElement itemToMoveOn) {
		Actions actions = new Actions(driver);
		actions.moveToElement(itemToMoveOn).build().perform();


	}
	
	public void waitForProductPageLoad(String urlKeyWord) {
		wait.until(ExpectedConditions.urlContains(urlKeyWord));
	}
	
	public void waitForPageLoad(String url) {
		wait.until(ExpectedConditions.urlToBe(url));
	}

	public void waitForPageTitle(String title) {
		wait.until(ExpectedConditions.titleIs(title));
	}

	public void waitForMessage(By element) {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		
	}

	public void waitTillClickable(By buttonElement) {
		wait.until(ExpectedConditions.elementToBeClickable(buttonElement));
	}

	public void waitForMiniCartOpen() {
		wait.until(ExpectedConditions.attributeToBe(miniCartBox, "class", "minicart-wrapper active"));
	}

	public void waitForProgress(WebElement progressWebElement) {
		wait.until(ExpectedConditions.attributeContains(progressWebElement, "class", "_complete"));
	}

	public void waitForLoaderDisable() throws InterruptedException {
		Thread.sleep(2000);
		//wait.until(ExpectedConditions.attributeToBe(loader, "style", "display: none;"));
	}

	public void waitForElementToBeVisible(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	/** Waiting Area End **/

	public void openShowCart() {
		showCartBtn.click();
		waitForMiniCartOpen();

	}
	
	public ShippingAddressPage proceedToCheckout() {
		checkoutBtnShowcart.click();
		waitForProductPageLoad("shipping");
        return new ShippingAddressPage(driver);
	}


	
	public void selectFromStaticDropdown(WebElement dropdownID, String optionToSelect) {
		Select dropdown = new Select(dropdownID);
		dropdown.selectByVisibleText(optionToSelect);
	}
	

	
	public void clickButtonByJavascriptExecutor(WebElement buttonElement) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", buttonElement);
	}
	


	public String getCurrentPageTitle(){
        return driver.getTitle();
	}

	public String validateSignUp() {
		createAccountBtn.click();
		waitForPageTitle("Create New Customer Account");
		return driver.getTitle();
	}

	public String getHeaderWelcomeMessage() {
		wait.until(ExpectedConditions.textToBePresentInElement(welcomeMessageText, "Welcome to DevBerry Commerce"));
		String welcomeText = welcomeMessageText.getText();
        return welcomeText;
	}

	public Map<String, Integer> getLinkStatusSummary() throws IOException {
		int validCount = 0;
		int brokenCount = 0;
		for (WebElement link : linkURL) {
			String url = link.getAttribute("href");
			if (url != null && !url.contains("javascript")) {
				String status = checkForBrokenLink(url);
				if ("Valid".equals(status)) {
					validCount++;
				} else if ("Broken".equals(status)) {
					brokenCount++;
				}
			}
		}
		Map<String, Integer> result = new HashMap<>();
		result.put("Valid", validCount);
		result.put("Broken", brokenCount);
		return result;
	}

	static {
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(X509Certificate[] certs, String authType) { }
					public void checkServerTrusted(X509Certificate[] certs, String authType) { }
				}
		};
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String checkForBrokenLink(String linkUrl) throws IOException {
		String linkStatus = null;
		URL url = new URL(linkUrl);
		HttpURLConnection connection;
		if (linkUrl.startsWith("https")) {
			connection = (HttpsURLConnection) url.openConnection();
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}
		connection.setConnectTimeout(3000);
		connection.connect();
		if(connection.getResponseCode() >= 400){
			linkStatus = "Broken";
		} else {
			linkStatus = "Valid";
		}
		return linkStatus;
	}

    // Java
    public boolean searchProduct(String productName) {
        searchBox.sendKeys(productName);
        searchBox.sendKeys(Keys.ENTER);
        waitForElementToBeVisible(searchProductsCount);
        boolean result = isSearchResultDisplayed(productName);
        if (result) {
            System.out.println("Search result for '" + productName + "' is displayed.");
        } else {
            System.out.println("Search result for '" + productName + "' is not displayed.");
        }
        return result;
    }

    // Java
    public boolean isSearchResultDisplayed(String desiredProductName) {
        List<WebElement> freshProductItems = driver.findElements(By.cssSelector(".product-item"));
        By itemNameLocator = By.cssSelector(".product-item-link");
        return freshProductItems.stream()
                .anyMatch(item -> item.findElement(itemNameLocator).getText().contains(desiredProductName));
    }


    /* Navigation Validation Methods */

    public String clickOnNavigationLink(String linkText) {
        WebElement link = navigationLinks.stream()
                .filter(navLink -> navLink.getText().equalsIgnoreCase(linkText))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Navigation link not found: " + linkText));
        link.click();
        waitForPageTitle(linkText);
        return driver.getTitle();
    }



}

