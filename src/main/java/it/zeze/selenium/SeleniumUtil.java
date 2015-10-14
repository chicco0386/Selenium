package it.zeze.selenium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtil {

	private static WebDriver driver = null;

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		SeleniumUtil.driver = driver;
	}

	public static void saveHTMLPage(String urlToSave, String fileDestPath) throws FileNotFoundException, IOException {
		SeleniumUtil.openDriver();
		SeleniumUtil.setDriverPage(urlToSave);
		String pageSource = driver.getPageSource();
		IOUtils.write(pageSource, new FileOutputStream(fileDestPath));
	}

	public static void saveCurrentPage(String fileDestPath) throws FileNotFoundException, IOException {
		String pageSource = driver.getPageSource();
		OutputStream os = new FileOutputStream(fileDestPath);
		IOUtils.write(pageSource, os);
		IOUtils.closeQuietly(os);
	}

	public static void setInputFieldByTagAttribute(String tag, String attributeName, String attributeValue, String valueToSet) {
		WebElement element = driver.findElement(By.xpath("//" + tag + "[@" + attributeName + "='" + attributeValue + "']"));
		element.sendKeys(valueToSet);
	}

	public static void clickLinkTagAttribute(String tag, String attributeName, String attributeValue) {
		SeleniumUtil.waitForXPathExpression("//" + tag + "[@" + attributeName + "='" + attributeValue + "']");
		WebElement element = driver.findElement(By.xpath("//" + tag + "[@" + attributeName + "='" + attributeValue + "']"));
		element.click();
	}

	public static void clickLink(String linkName) {
		WebElement element = driver.findElement(By.linkText(linkName));
		element.click();
	}

	public static void setDriverPage(String url) {
		SeleniumUtil.openDriver();
		driver.get(url);
	}

	public static void openDriver() {
		if (driver == null) {
			// Setto il Proxy per PC Lavoro
			// String PROXY = "localhost:3128";
			// org.openqa.selenium.Proxy proxy = new
			// org.openqa.selenium.Proxy();
			// proxy.setHttpProxy(PROXY)
			// .setFtpProxy(PROXY)
			// .setSslProxy(PROXY);
			// DesiredCapabilities cap = new DesiredCapabilities();
			// cap.setCapability(CapabilityType.PROXY, proxy);
			// driver = new FirefoxDriver(cap);
			String pathFirefoxBin = "/home/enrico/Scrivania/ZeZe/firefox_31.0/firefox";
			File firefoxBin = new File(pathFirefoxBin);
			if (firefoxBin.exists()) {
				System.setProperty("webdriver.firefox.bin", pathFirefoxBin);
			}
			driver = new FirefoxDriver();
		}
	}

	public static void closeDriver() {
		if (driver != null) {
			driver.close();
			driver.quit();
		}
	}

	public static void waitForXPathExpression(String xpath) {
		WebDriverWait driverWait = new WebDriverWait(driver, 30);
		driverWait.until(new MyCondition(xpath));
		driverWait.ignoring(NoSuchElementException.class);
	}

	public static void main(String[] artgs) {
		try {
			BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));
			SeleniumUtil.setDriverPage("http://www.fantagazzetta.com/");
			System.out.print("Inserisci user: ");
			String user = dataIn.readLine();
			System.out.print("Inserisci pass: ");
			String pass = dataIn.readLine();
			SeleniumUtil.setInputFieldByTagAttribute("input", "class", "login-user", user);
			SeleniumUtil.setInputFieldByTagAttribute("input", "class", "login-password", pass);
			SeleniumUtil.clickLinkTagAttribute("input", "class", "login-submit");
			SeleniumUtil.setDriverPage("http://www.fantagazzetta.com/voti-fantagazzetta-serie-A");
			SeleniumUtil.waitForXPathExpression("//div[@id='allvotes']");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			SeleniumUtil.closeDriver();
		}
	}
}
