package com.ooyala.playback.amf.midroll;

import com.ooyala.playback.page.*;
import com.ooyala.playback.url.UrlObject;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackMidRollAdsTests extends PlaybackWebTest {

	public PlaybackMidRollAdsTests() throws OoyalaException {
		super();
	}

	private EventValidator event;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private SetEmbedCodeValidator setEmbedCodeValidator;
    private MidrollAdValidator midrollAdValidator;

	@Test(groups = {"amf","midroll"}, dataProvider = "testUrls")
	public void verifyMidRoll(String testName, UrlObject url) throws OoyalaException {
		
		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && playValidator.waitForPage();

			injectScript();

			result = result && playValidator.validate("playing_1", 60000);

			result = result && midrollAdValidator.validateMidrollAd(url);
			
			if(testName.contains("SetEmbedCode")){
//				result = result && setEmbedCodeValidator.validate("setEmbedmbedCode",6000); TODO: Resolve NPE
			}else{
				result = result && seekValidator.validate("seeked_1", 160000);
				result = result && event.validate("played_1", 160000);
			}

		} catch (Exception e) {
			e.printStackTrace();
			extentTest.log(LogStatus.FAIL, e);
			result = false;
		}

		Assert.assertTrue(result, "Verified");
	}
}