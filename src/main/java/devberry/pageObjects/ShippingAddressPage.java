package devberry.pageObjects;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import devberry.AbstractComponents.AbstractComponent;

public class ShippingAddressPage extends AbstractComponent {
	WebDriver driver;

	public ShippingAddressPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".shipping-address-items div")
	List<WebElement> addresses;

	@FindBy(css = ".items-in-cart")
	WebElement expandBtn;

	@FindBy(css = ".details-qty .value")
	WebElement quantityText;

	@FindBy(css = ".col-price .price")
	WebElement systemShippingCharges;

	@FindBy(css = "button[class*='continue']")
	WebElement nextBtn;

	@FindBy(css = "input[name='company']")
	WebElement companyName;

	@FindBy(css = "input[name='street[0]']")
	WebElement streetAddress1;

	@FindBy(css = "input[name='street[1]']")
	WebElement streetAddress2;

	@FindBy(css = "select[name='country_id']")
	WebElement countryDropdown;

	@FindBy(css = "select[name='region_id']")
	WebElement stateDropdown;

	@FindBy(css = "input[name='city']")
	WebElement city;

	@FindBy(css = "input[name='postcode']")
	WebElement postalCode;

	@FindBy(css = "input[name='telephone']")
	WebElement phoneNumber;

	@FindBy(css = "#shipping-method-buttons-container div button")
	WebElement saveAddress;

	@FindBy(css = ".opc-progress-bar-item:nth-child(1)")
	WebElement progressBar;

	@FindBy(css = ".shipping-information-content")
	WebElement shippingInfo;

	public void enterNewAddress(HashMap<String, String> input) {
		companyName.clear();
		companyName.sendKeys(input.get("companyName"));
		streetAddress1.clear();
		streetAddress1.sendKeys(input.get("streetAddress1"));
		streetAddress2.clear();
		streetAddress2.sendKeys(input.get("streetAddress2"));
		
		selectFromStaticDropdown(countryDropdown, input.get("desiredCountry"));
		selectFromStaticDropdown(stateDropdown, input.get("DesiredState"));
		
		city.clear();
		city.sendKeys(input.get("city"));
		
		postalCode.clear();
		postalCode.sendKeys(input.get("postalCode"));
		
		phoneNumber.clear();
		phoneNumber.sendKeys(input.get("phoneNumber"));
		
		clickButtonByJavascriptExecutor(saveAddress);
		waitForProgress(progressBar);

	}

	public void expandOrderSummary() {
		expandBtn.click();

	}

	public double calculateShippingCharges() {
		expandOrderSummary();
		String qtyText = quantityText.getText();
		int qtyInCart = Integer.parseInt(qtyText);

        return qtyInCart * 5;
	}

	public double getShippingChargesOfSystem() {

		String shippingCostText = systemShippingCharges.getText();
        return Double.parseDouble(shippingCostText.substring(1));
	}

	public PaymentPage proceedToPayment() {
		nextBtn.click();
		waitForPageLoad("https://magento247-pub.test/checkout/#payment");
        return new PaymentPage(driver);
	}

	public Boolean checkForAvailableAddresses(HashMap<String, String> input) throws InterruptedException {
		Boolean addressStatus = null;
		if(addresses.isEmpty()) {
			enterNewAddress(input);
			addressStatus =shippingInfo.getText().contains(input.get("companyName"));
			return addressStatus;
		}else if(addresses.size() >= 1) {
			WebElement foundAddress = addresses.stream().filter(address -> address.getText().contains(input.get("companyName"))).findAny().orElse(null);
			addressStatus = foundAddress.getText().contains(input.get("companyName"));
			return addressStatus;
		}
		waitForLoaderDisable();
		return addressStatus;
	}
}
