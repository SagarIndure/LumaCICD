package devberry.Tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import devberry.TestComponents.BaseTest;
import devberry.pageObjects.LoginPage;
import devberry.pageObjects.OrderConfirmationPage;
import devberry.pageObjects.PaymentPage;
import devberry.pageObjects.ProductDetailsPage;
import devberry.pageObjects.ShippingAddressPage;

public class SubmitOrder extends BaseTest {

	@Test(dataProvider = "getData")
	public void submitOrderTest(HashMap<String, String> input) throws IOException, InterruptedException {
		LoginPage loginPage = landingPage.getLoginPage();
		loginPage.loginApplication(input.get("email"), input.get("password"));
		landingPage.goToSection();
		ProductDetailsPage productDetailsPage = landingPage.addItemToCart(input.get("desiredItemName"));
		landingPage.waitForProductPageLoad(input.get("desiredItemName").toLowerCase().substring(0, 4));
		productDetailsPage.getDesiredSize(input.get("desiredSizeName"));
		productDetailsPage.getDesiredColour(input.get("desiredColor"));
		productDetailsPage.setQuantity(input.get("desiredQty"));
		String cartMsg = productDetailsPage.addToCart();
		Assert.assertEquals(cartMsg, input.get("expectedCartMsg"));
		productDetailsPage.openShowCart();
		ShippingAddressPage shippingAddressPage = productDetailsPage.proceedToCheckout();
		Boolean addressStatus = shippingAddressPage.checkForAvailableAddresses(input);
		Assert.assertTrue(addressStatus);
		double appliedShippingCharges = shippingAddressPage.calculateShippingCharges();
		double displayedShippingCost = shippingAddressPage.getShippingChargesOfSystem();
		Assert.assertEquals(appliedShippingCharges, displayedShippingCost);
		PaymentPage paymentPage = shippingAddressPage.proceedToPayment();
		double subTotalAmountTable = paymentPage.getSubtotalAmount();
		double shippingAmountTable = paymentPage.getShippingAmount();
		double discountAmountTable = paymentPage.getDiscountAmount();
		double calculatedTotal = paymentPage.calculateOrderTotal(subTotalAmountTable, shippingAmountTable);
		double finalAmount = paymentPage.calculateDiscount(calculatedTotal, discountAmountTable);
		double systemOrderAmount = paymentPage.getTotalSystemAmount();
		Assert.assertEquals(finalAmount, systemOrderAmount);
		OrderConfirmationPage orderConfirmationPage = paymentPage.placeOrder();
		String successMsg = orderConfirmationPage.getConfirmationMessage();
		Assert.assertEquals(input.get("expectedConfirmationMessage"), successMsg);
		String orderID = orderConfirmationPage.getOrderID();
		System.out.println("Your Order ID is : " + orderID);

	}
	



	@DataProvider
	public Object[][] getData() throws IOException{
		
		List<HashMap<String,String>> data = getJsonDataToMap(System.getProperty("user.dir")+"//src//test//java//devberry//data//submitOrder.json");
		return new Object[][] {{data.get(0)},{data.get(1)}};
		
	}



}
