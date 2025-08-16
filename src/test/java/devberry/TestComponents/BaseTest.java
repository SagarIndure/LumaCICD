package devberry.TestComponents;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;


import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import devberry.pageObjects.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class BaseTest {
	public WebDriver driver;
	public LandingPage landingPage;

	public WebDriver initializeDriver() throws IOException {

		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/java/devberry/resources/GlobalData.properties");
		Properties prop = new Properties();

		prop.load(fis);
		String browserName = System.getProperty("browser")!= null ? System.getProperty("browser") : prop.getProperty("browser");
		//String browserName = prop.getProperty("browser");

		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--ignore-certificate-errors");
			options.addArguments("--allow-insecure-localhost");
			options.addArguments("--disable-save-password-bubble");
			options.setExperimentalOption("prefs",
					Map.of("credentials_enable_service", false, "profile.password_manager_enabled", false));
			driver = new ChromeDriver(options);
			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int screenWidth = screenSize.width;
			int screenHeight = screenSize.height;

			int xOffset = (int)(screenWidth * 0.17); // 20% from left
			int windowWidth = (int)(screenWidth * 0.83); // 80% width

			int windowHeight = screenHeight;
			driver.manage().window().setSize(new Dimension(windowWidth, windowHeight));
			driver.manage().window().setPosition(new Point(xOffset, 0));

		} else if (browserName.equalsIgnoreCase("safari")) {
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();

			java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int screenWidth = screenSize.width;
			int screenHeight = screenSize.height;

			int xOffset = (int)(screenWidth * 0.17); // 20% from left
			int windowWidth = (int)(screenWidth * 0.83); // 80% width

			int windowHeight = screenHeight;
			driver.manage().window().setSize(new Dimension(windowWidth, windowHeight));
			driver.manage().window().setPosition(new Point(xOffset, 0));


		} else if (browserName.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		return driver;
	}

	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
		String jsonContent = FileUtils.readFileToString(new File(filePath),
				StandardCharsets.UTF_8);

		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});
		return data;
	}

	@BeforeMethod
	public LandingPage launchApplication() throws IOException {
		driver = initializeDriver();
		landingPage = new LandingPage(driver);
		landingPage.goTo();
		return landingPage;
	}

	@AfterMethod
	public void tearDown() throws IOException {

		driver.quit();
	}
	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File destination = new File(System.getProperty("user.dir")+"//reports//screenshots//" + testCaseName +".png");
		FileUtils.copyFile(source, destination);
		return System.getProperty("user.dir")+"//reports//screenshots//" + testCaseName +".png";
	}



}
