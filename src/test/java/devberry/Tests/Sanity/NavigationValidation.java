package devberry.Tests.Sanity;

import devberry.TestComponents.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavigationValidation extends BaseTest {

    @Test
    public void navigationValidationTest(){
        landingPage.goTo();
        String pageTitle = landingPage.clickOnNavigationLink("What's New");
        Assert.assertTrue(pageTitle.contains("What's New"), "Navigation to 'What's New' section failed");
         
       
    }
}
