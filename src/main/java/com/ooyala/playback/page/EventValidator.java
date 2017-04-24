package com.ooyala.playback.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by soundarya on 11/14/16.
 */
public class EventValidator extends PlayBackPage implements PlaybackValidator {

	public static Logger logger = Logger.getLogger(EventValidator.class);

	public EventValidator(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		/**
		 * Here we will tell Facile to add the page elements of our Login Page
		 */
		addElementToPageElements("discovery");
		addElementToPageElements("play");
		addElementToPageElements("pause");
		addElementToPageElements("replay");
		addElementToPageElements("controlbar");
		addElementToPageElements("fullscreen");
		addElementToPageElements("adPodEnd");
		addElementToPageElements("startscreen");
	}

	public boolean validate(String element, int timeout) throws Exception {

		if (!loadingSpinner()) {
			extentTest.log(LogStatus.FAIL, "Loading spinner seems to be there for a really long time.");
			return false;
		}

		if (waitOnElement(By.id(element), timeout)) {
			extentTest.log(LogStatus.PASS, "Wait on element : " + element);
			return true;
		}
		extentTest.log(LogStatus.FAIL, "Wait on element : " + element + " failed after " + timeout + " ms");
		return false;
	}

	public boolean eventAction(String element) throws Exception {
		return clickOnIndependentElement(element);
	}

	public void validateElement(String element, int timeout) throws Exception {
		if (waitOnElement(element, timeout)) {
			extentTest.log(LogStatus.PASS, "Wait on element : " + element);
		} else {
			extentTest.log(LogStatus.FAIL, "Wait on element : " + element + " failed after " + timeout + " ms");
		}

	}

	public boolean validateElementPresence(String element) throws Exception {
		if (isElementPresent(By.id(element))) {
			return true;
		}
		return false;
	}

	public boolean waitForVideoToPlay(int secs) {
		int count = 0;
		double playTime = Double
				.parseDouble(((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
		while (playTime <= secs) {
			playTime = Double.parseDouble(
					((JavascriptExecutor) driver).executeScript("return pp.getPlayheadTime();").toString());
			if (count == (secs * 4)) {
				return false;
			}
		}
		return true;
	}

}