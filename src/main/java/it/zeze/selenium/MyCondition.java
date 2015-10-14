package it.zeze.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class MyCondition implements ExpectedCondition<WebElement> {

	private String xpath = "";

	public MyCondition(String xpath) {
		this.xpath = xpath;
	}

	public WebElement apply(WebDriver d) {
		return d.findElement(By.xpath(xpath));
	}

}
