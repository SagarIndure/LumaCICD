package devberry.Tests.Sanity;

import devberry.TestComponents.BaseTest;
import devberry.pageObjects.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class LoginValidation extends BaseTest {

    @Test(dataProvider = "getData")
    public void loginValidationTest(HashMap<String, String> input) {
        LoginPage loginPage = landingPage.getLoginPage();
        Assert.assertEquals(landingPage.getCurrentPageTitle(), "Customer Login");
        loginPage.loginApplication(input.get("email"), input.get("password"));

    }


    @DataProvider
    public Object[][] getData() throws IOException {

        List<HashMap<String,String>> data = getJsonDataToMap(System.getProperty("user.dir")+"//src//test//java//devberry//data//submitOrder.json");
        return new Object[][] {{data.get(0)},{data.get(1)}};

    }
}
