package com.ooyala.playback.amf.VAST;

import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.page.VolumeValidator;
import com.ooyala.playback.page.action.FullScreenAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackAdWrapperTests extends PlaybackWebTest {
	
	//TODO :skin config in data provider
	

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private PauseAction pauseAction;
	private FullScreenAction fullScreenAction;
	private VolumeValidator volumeValidator;

	public PlaybackAdWrapperTests() throws OoyalaException {
		super();
	}

	@DataProvider(name = "testUrls")
	public Object[][] getTestData() {

		return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
				nodeList);
	}
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyPlaybackAdWrapper(String testName, String url) throws Exception {
		
		boolean result = false;
		event = pageFactory.getEventValidator();
		playAction = pageFactory.getPlayAction();
		playValidator = pageFactory.getPlayValidator();
		seekValidator = pageFactory.getSeekValidator();
		pauseAction = pageFactory.getPauseAction();
		fullScreenAction = pageFactory.getFullScreenAction();
		volumeValidator = pageFactory.getVolumeValidator();
		
		try {
			
			driver.get(url);
			if (!getPlatform().equalsIgnoreCase("android")) {
				driver.manage().window().maximize();
			}

			playValidator.waitForPage();
			Thread.sleep(10000);
			
			//TODO
			injectScript("http://192.168.0.43:8080/common.js");
			injectScript("http://192.168.0.43:8080/amf/amf.js");
			
			playValidator.validate("playing_1", 190);
			extentTest.log(LogStatus.PASS, "Main video started to play");
			sleep(500);
			
			pauseAction.startAction();
			fullScreenAction.startAction();
			
			sleep(2000);
			 
			playAction.startAction();
			
			volumeValidator.validate("", 60);
			seekValidator.validate("seeked_1", 190);
			event.validate("played_1", 190);
			
			
			result = true;
			
			extentTest.log(LogStatus.PASS, "Main Video played successfully");
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback Ad Wrapper tests failed");
		
	}
}
