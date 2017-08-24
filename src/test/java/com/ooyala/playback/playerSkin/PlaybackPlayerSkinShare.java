package com.ooyala.playback.playerSkin;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.PlayerSkinShareValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by Gautham
 */
public class PlaybackPlayerSkinShare extends PlaybackWebTest {
	private PlayValidator play;
	private PlayerSkinShareValidator skinShareValidator;

	private static Logger logger = Logger.getLogger(PlaybackPlayerSkinShare.class);

	public PlaybackPlayerSkinShare() throws OoyalaException {
		super();
	}

	@Test(groups = "PlayerSkin", dataProvider = "testUrls")
	public void testFCCClosedcaption(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;
		try {

			String urlLink = url.getUrl();

			driver.get(urlLink);

			result = result && play.waitForPage();

			injectScript();

			result = result && play.validate("playing_1", 60000);

			result = result && skinShareValidator.validateShareScreen();

		} catch (Exception e) {
			logger.error(e);
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback FCC CC tests failed :" + testName);
	}
}
