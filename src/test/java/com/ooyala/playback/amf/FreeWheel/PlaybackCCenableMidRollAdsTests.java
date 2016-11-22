package com.ooyala.playback.amf.FreeWheel;

import static com.relevantcodes.extentreports.LogStatus.PASS;
import static java.lang.Thread.sleep;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.CCValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlGenerator;
import com.ooyala.qe.common.exception.OoyalaException;

public class PlaybackCCenableMidRollAdsTests extends PlaybackWebTest{

	public PlaybackCCenableMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayAction playAction;
	private PlayValidator playValidator;
	private SeekAction seekAction;
	private PauseAction pauseAction;
	private CCValidator ccValidator;
	
	@DataProvider(name = "testUrls")
	public Object[][] getTestData() {

		return UrlGenerator.parseXmlDataProvider(getClass().getSimpleName(),
				nodeList);
	}
	
	@Test(groups = "amf", dataProvider = "testUrls")
	public void verifyCCenableMidRoll(String testName, String url) throws Exception {
		boolean result = false;
		event = pageFactory.getEventValidator();
		playAction = pageFactory.getPlayAction();
		playValidator = pageFactory.getPlayValidator();
		seekAction = pageFactory.getSeekAction();
		pauseAction = pageFactory.getPauseAction();
		ccValidator = pageFactory.getCCValidator();
		
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
			
			playAction.startAction();
			sleep(1000);
			event.validate("playing_1", 60);
			
		    logger.info("Video started playing");

		    sleep(22000);
		    
		    event.validate("videoPlaying_1", 190);
		    event.validate("MidRoll_willPlaySingleAd_1", 190);

		    logger.info("Midroll Ad started to play");
		    
		    event.validate("singleAdPlayed_1", 190);
		    
//		    TODO
		        /*if (Description.equalsIgnoreCase("Midroll_Bitmovin_Pulse_CC"))
		        {
		            waitForElement(webDriver, "singleAdPlayed_2", 190);
		        }else {
		            waitForElement(webDriver, "singleAdPlayed_1", 190);
		        }*/

		    logger.info("Midroll Ad ended");
		    
		    event.validate("videoPlaying_1", 190);
		    pauseAction.startAction();
		    
		    ccValidator.validate("cclanguage",60);
		    event.validate("videoPlaying_1", 190);
		    
		    seekAction.seek(10, true);
		    
		    event.validate("seeked_1", 190);
		    event.validate("played_1", 250);


		    extentTest.log(PASS, "Video Played");

		    extentTest.log(PASS, "Verified MidrollAdsTest");
			
		}catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		Assert.assertTrue(result, "Playback CC Enabled MidRoll Ads tests failed");
		
	}
}
