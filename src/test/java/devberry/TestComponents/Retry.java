package devberry.TestComponents;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    /**
     * @param result The result of the test method that just ran.
     * @return
     */

    int count = 0;
    int maxTry = 1; // Set the maximum number of retries
    @Override
    public boolean retry(ITestResult result) {
        if (count<maxTry){
            count++;
            return true; // Retry the test method
        }
        return false;
    }
}
