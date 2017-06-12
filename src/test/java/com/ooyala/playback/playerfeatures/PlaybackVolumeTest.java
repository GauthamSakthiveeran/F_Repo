package com.ooyala.playback.playerfeatures;

import com.ooyala.playback.page.*;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/16/16.
 */
public class PlaybackVolumeTest extends PlaybackWebTest {

	private PlayValidator play;
	private SeekValidator seek;
	private PlayAction playAction;
	private EventValidator eventValidator;
	private VolumeValidator volumeValidator;
	private IsAdPlayingValidator isAdPlayingValidator;
	private AnalyticsValidator analyticsValidator;

	public PlaybackVolumeTest() throws OoyalaException {
		super();
	}

	@Test(groups = "playerFeatures", dataProvider = "testUrls")
	public void testVolume(String testName, UrlObject url) throws OoyalaException {

		boolean result = true;

		try {
			driver.get(url.getUrl());

			result = result && play.waitForPage();

			if (url.getVideoPlugins().contains("ANALYTICS"))
				analyticsValidator.getConsoleLogForAnalytics();

			injectScript();

			result = result && playAction.startAction();

			result = result && eventValidator.loadingSpinner();

			Boolean isAdplaying = isAdPlayingValidator.validate("",60);
			if (isAdplaying) {
				result = result && volumeValidator.validate("", 60000);

				result = result && eventValidator.validate("adPodEnded_1", 20000);
			}

            result = result && eventValidator.validate("playing_1", 60000);

            result = result && eventValidator.playVideoForSometime(3);

			result = result && volumeValidator.validate("", 60000);

			result = result && seek.validate("seeked_1", 60000);

			result = result && eventValidator.validate("played_1", 60000);

			if (url.getVideoPlugins().contains("ANALYTICS"))
				result = result && analyticsValidator.validate("analytics_volume_changed_1", 5000);

		} catch (Exception e) {
			logger.error(e.getMessage());
			extentTest.log(LogStatus.FAIL, e.getMessage());
			result = false;
		}
		Assert.assertTrue(result, "Playback Volume tests failed");
	}

}
