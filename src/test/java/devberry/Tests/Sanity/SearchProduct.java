package devberry.Tests.Sanity;

import com.fasterxml.jackson.databind.ser.Serializers;
import devberry.TestComponents.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchProduct extends BaseTest {

    @Test
    public void searchProductTest() {
        Boolean searchResult = landingPage.searchProduct("Watch");
        Assert.assertTrue(searchResult, "Search result for 'jacket' is not displayed.");
    }
}
