package com.ooyala.playback.page;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

/**
 * Created by snehal on 28/11/16.
 */
public class DifferentElementValidator extends PlayBackPage implements
		PlaybackValidator {
	public static Logger logger = Logger.getLogger(DestroyValidator.class);

	public DifferentElementValidator(WebDriver webDriver) {
		super(webDriver);
		PageFactory.initElements(driver, this);
		addElementToPageElements("element");
	}

	@Override
	public boolean validate(String element, int timeout) throws Exception {
		List<WebElement> ele = getWebElementsList(element);
		String element1_id = ele.get(0).getAttribute("id");
		String element2_id = ele.get(1).getAttribute("id");
		Assert.assertNotEquals(element1_id, element2_id,
				"Both should not have same id");
		return true;
	}
}
