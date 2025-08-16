package devberry.Tests;

import devberry.TestComponents.Retry;
import org.testng.Assert;
import org.testng.annotations.Test;

import devberry.TestComponents.BaseTest;
import devberry.pageObjects.LoginPage;

public class LoginErrorValidation extends BaseTest{
	
	@Test(retryAnalyzer = Retry.class)
	public void invalidUserEmail() {
		String userEmail = "sagarindure@gmail.com";
		String userPassword = "Circuit@#32";
		
		LoginPage loginPage = landingPage.getLoginPage();
		loginPage.loginApplication(userEmail, userPassword);
		String errorMessage = loginPage.getErrorMessage();
		Assert.assertEquals("The account sign-in was incorrect or your account is disabled temporarily. Please wait and try again later.",
				errorMessage);
	}

}
