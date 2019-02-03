package at.fhv.weather;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Step implementation for the sentiment analysis UAT tests
 */
public class WeatherAnalysisSteps {

	private WebDriver driver;

	/**
	 * Setup the firefox test driver. This needs the environment variable
	 * 'webdriver.gecko.driver' with the path to the geckodriver binary
	 */
	@Before
	public void before(Scenario scenario) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platform", "WIN10");
		capabilities.setCapability("version", "64");
		capabilities.setCapability("browserName", "firefox");
		capabilities.setCapability("name", scenario.getName());

		if (!scenario.getName().endsWith("(video)")) {
			capabilities.setCapability("headless", true);
		}


		driver = new RemoteWebDriver(
				new URL("http://" + System.getenv("TESTINGBOT_CREDENTIALS") + "@hub.testingbot.com/wd/hub"),
				capabilities);

		// prevent errors if we start from a sleeping heroku instance
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	}

	/**
	 * Shutdown the driver
	 */
	@After
	public void after() {
		driver.quit();
	}

	@Given("^Open (.*?)$")
	public void openUrl(String url) {
		driver.navigate().to(url);
	}

	@Given("^Login with user '(.*?)'$")
	public void login(String email) {
		WebElement emailField = driver.findElement(By.id("name"));
		emailField.sendKeys(email);
		driver.findElement(By.id("loginBtn")).click();
	}

	@When("^Check the weather '(.*?)'$")
	public void analyzeText(String text) {
		WebElement textField = driver.findElement(By.id("locationSearch"));
		textField.clear();
		textField.sendKeys(text);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("analyzeBtn")));
		button.click();

	}

	@Then("^The Location should be (.*?)$")
	// wait until the result has been received
	public void checkLocation(String location) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("analyzeBtn")));
		WebElement weatherLocation = driver.findElement(By.id("location"));
		assertEquals(location, weatherLocation.getText());
	}


	@Then("^The Weather should be (.*?)$")
	// wait until the result has been received
	public void checkWeather(String location) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("analyzeBtn")));
		WebElement weatherLocation = driver.findElement(By.id("location"));
		assertEquals(location, weatherLocation.getText());
	}



	@When("^I press logout$")
	public void logout() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement logoutLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("logoutLink")));
		logoutLink.click();

		// wait until popup is visible
		WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("logoutBtn")));
		logoutBtn.click();
	}

	@Then("^I see the login page$")
	public void checkLoginPage() {
		assertTrue(driver.findElements( By.id("loginBtn") ).size() != 0);
	}

	@When("^Navigate to history$")
	public void navigateToHistory() {
		driver.findElement(By.linkText("History")).click();
	}


	@Then("^The ([0-9]). row shows the history item with text '(.*?)' has weatherinformations")
	public void checkHistoryItem(int row, String text) {
		WebElement textCell = driver.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[1]"));
		WebElement weatherCell = driver.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[2]"));
		String weather = weatherCell.getText();
		assertEquals(text, textCell.getText());
		assertTrue(weather.contains("temperature"));
	}

}
