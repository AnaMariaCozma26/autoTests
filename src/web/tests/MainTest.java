package web.tests;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
//importing possible error messages
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
//importing the drivers' classes that will be used
import org.openqa.selenium.WebDriver; //importing the interface class WebDriver
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver; //importing the class ChromeDriver - implementing the methods from WebDriver for the Chrome web browser
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;


public class MainTest {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

		// creating a new text file in which the test results will be written
		PrintWriter writer = new PrintWriter("testResults.txt", "UTF-8");

		// setting the properties for the browser that will be used - the driver and its
		// executable file path
		System.setProperty("webdriver.chrome.driver", "D:\\selenium java\\webAutoTest\\src\\drivers\\chromedriver.exe");

		// running the test with the headless option on - this will make the test run
		// faster because the test will not wait for the UI to load
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setHeadless(true);

		// creating an instance of the class ChromeDriver
		// WebDriver driver = new ChromeDriver(chromeOptions);
		WebDriver driver = new ChromeDriver();

		// creating an instance of the JavaScript driver
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// setting the web address that will be accessed
		String webAdress = "https://www.bitdefender.com/";

		// checking if the link adress that will be used is valid - the string has a
		// value that starts with the protocol
		writer.println("Test1: Launching the webpage");
		if (webAdress != null && (webAdress.startsWith("http://") || webAdress.startsWith("https://"))) {
			// opening the web browser and launching the desired webpage
			try {
				driver.get(webAdress);
			} catch (InvalidArgumentException i) {
				writer.println("Failed.");
				System.out.println("Invalid web adress could not be launched!");
				return;
			}
		} else {
			writer.println("Passed. Web adress was launched.");
		}

		// maximizing the browser to the window's scale
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// getting the page title
		String title = driver.getTitle();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// closing the cookies pop-up
		driver.findElement(By.id("CybotCookiebotDialogBodyLevelButtonLevelOptinDeclineAll")).click();

		// finding the element Home -> See Solutions
		writer.println("Test2: Clicking on the Home -> See Solutions button");
		// creating an instance of the element that needs to be found with Xpath and
		// Class
		WebElement solutions = driver.findElement(By
				.xpath("//a[@href='//www.bitdefender.com/solutions/' and @class='button-1 d-sm-inline-block d-none']"));

		// scroll the page until the element that needs to be found is visible in the UI
		js.executeScript("arguments[0].scrollIntoView();", solutions);
		solutions.click();

		title = driver.getTitle();
		if (title.equals("Bitdefender Security Software Solutions for Home Users")) {
			writer.println("Passed. Solutions page was launched.");
			System.out.println(title);
		} else {
			writer.println("Failed.");
		}

		writer.println("Test3: Click on MultiPlatform button");
		// creating an instance of the element that needs to be found by its id - the
		// MultiPlatform Security button
		WebElement multiPlatformBtn = driver.findElement(By.id("mp-scroll"));
		multiPlatformBtn.click();

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// closing the pop-up
		try {
			WebElement closePopup = driver.findElement(By.className("AGPopupBoxClose"));
			if (closePopup != null) {
				closePopup.click();
			}
		} catch (NoSuchElementException n) {
		}

		// find the value of the price of the security package to check with the one
		// form the cart
		WebElement multiPlatformSection = driver.findElement(By.xpath("//div[@id='MultiplatformSecurity']"));
		if (multiPlatformSection != null) {
			writer.println("Passed. Solutions section was launched.");
		} else {
			writer.println("Failed");
		}

		WebElement priceMPlatform = multiPlatformSection
				.findElement(By.xpath("//span[@class='productS__oldprice oldprice_elite']"));

		// scrolling the page until the element is found
		js.executeScript("arguments[0].scrollIntoView();", priceMPlatform);

		Double priceMPlatformValue = Double.parseDouble(priceMPlatform.getText().replaceAll("[^0-9]", ""));

		writer.println("Test4: Click on BuyNow button");
		// click the button buy now for the premium package of security
		WebElement buyNow = driver.findElement(By.xpath("//*[@id='MultiplatformSecurity']/div[2]/div[1]/a[1]"));
		buyNow.click();

		title = driver.getTitle();
		if (title.equals("Bitdefender - Get Protection")) {
			writer.println("Passed. The cart is now visible.");
		} else {
			writer.println("Failed");
		}

		// creating an instance of a Select object to select the currency of the prices
		// in the cart
		Select dropdown = new Select(driver.findElement(By.xpath("//*[@id=\"storeApp\"]/div[1]/div[2]/section[1]/div/div[3]/span[2]/select")));

		// from the drowpdown, selecting USD currency to check with the value soung in
		// the MultiPlatform page
		dropdown.selectByValue("USD");

		// get the value of the price of the products from the cart
		WebElement priceCart1 = driver.findElement(By.xpath("//span[@class='productPrice ng-binding']"));
		js.executeScript("arguments[0].scrollIntoView();", priceCart1);
		Double priceCart1Value = Double.parseDouble(priceCart1.getText().replaceAll("[^0-9]", ""));

		writer.println("Test5: Check the price from the cart");
		// check if the prices match and print a corresponding message in the console
		if (priceMPlatformValue.equals(priceCart1Value)) {
			System.out.println("Correct price for one product");
			writer.println("Passed. Correct price");
		} else {
			System.out.println("Incorrect price for one product");
			writer.println("Failed");
		}

		writer.println("Test6: Changing the quantity in the cart to two products");
		// changing the quantity of the premium security package in the cart to 2
		// products
		WebElement quantity = driver.findElement(By.xpath("//input[@id='qty_21642367']"));
		quantity.clear();
		quantity.sendKeys("2");

		// updating the quantity in the cart
		WebElement updateQuantity = driver.findElement(By.xpath("//span[@ng-if='i.p_qty_allow']"));
		updateQuantity.click();

		// getting the value of the price for two products
		WebElement priceCart2 = driver.findElement(By.xpath("//span[@class='productPrice ng-binding']"));
		Double priceCart2Value = Double.parseDouble(priceCart2.getText().replaceAll("[^0-9]", ""));

		// check if the price matches the quantity and print a corresponding message in
		// the console
		if (priceCart2Value.equals(priceCart1Value * 2)) {
			System.out.println("Correct price for two products");
			writer.println("Passed. Correct price");
		} else {
			System.out.println("Incorrect price for two products");
			writer.println("Failed");
		}

		writer.println("Test7: Deleting the products from the cart");
		// remove the products from the cart
		WebElement wrapper = driver.findElement(By.xpath("//span[@id='pid_21642367']"));
		wrapper.findElement(By.xpath("//i[@class='fa fa-trash-o']")).click();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		// checking the title because after deletion the user is redirected to another
		// page
		title = driver.getTitle();
		if (title.equals("Bitdefender Security Software Solutions for Home Users")) {
			System.out.println(title);
			writer.println("Passed. Products were deleted");
		} else {
			writer.println("Failed");
		}
		
		writer.close();
		// closing the web browser
		driver.close();
	}

}
