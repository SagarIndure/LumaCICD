package devberry.Tests.Sanity;

import devberry.TestComponents.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class PageLoad extends BaseTest {

    @Test
    public void homePageLoadTest(){
        Assert.assertEquals(landingPage.getCurrentPageTitle(), "Home Page");
    }

    @Test
    public void createAccountPageLoadTest() {
        String pageTitle = landingPage.validateSignUp();
        Assert.assertEquals(pageTitle, "Create New Customer Account");
    }

    @Test
    public void homePageContentTest() throws IOException {
        String headerMessage = landingPage.getHeaderWelcomeMessage();
        Assert.assertEquals(headerMessage, "Welcome to DevBerry Commerce");

        Map<String, Integer> linkStatusSummary = landingPage.getLinkStatusSummary();
        Assert.assertEquals(linkStatusSummary.get("Broken").intValue(), 0, "There are broken links on the page.");
        Assert.assertTrue(linkStatusSummary.get("Valid") > 0, "No valid links found on the page.");
    }


}
