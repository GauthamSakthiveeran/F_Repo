package com.ooyala.playback.alice;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.qe.common.exception.OoyalaException;

/**
 * Created by soundarya on 11/17/16.
 */
public class PlaybackReplayVideoTests extends PlaybackWebTest {

	public PlaybackReplayVideoTests() throws OoyalaException {
		super();
	}

	@Test(groups = "alice", dataProvider = "testUrls")
	public void testVideoReplay(String testName, String url)
			throws OoyalaException {

		boolean result = false;
		PlayValidator play = pageFactory.getPlayValidator();
		SeekValidator seek = pageFactory.getSeekValidator();
		EventValidator eventValidator = pageFactory.getEventValidator();
		ReplayValidator replayValidator = pageFactory.getReplayValidator();

		try {
			driver.get(url);

			play.waitForPage();

			injectScript("http://10.11.66.55:8080/alice.js");

			play.validate("playing_1", 60);

			logger.info("video is playing");

			Thread.sleep(2000);

			seek.validate("seeked_1", 60);

			logger.info("video seeked");

			eventValidator.validate("played_1", 200);

			logger.info("video played");

			replayValidator.validate("replay_1", 60);

			logger.info("video replayed");

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertTrue(result, "Alice basic playback tests failed");
	}
}
