package com.ooyala.playback.playerfeatures;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.ReplayValidator;
import com.ooyala.playback.page.SeekValidator;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

public class PlaybackReplayVideoTests extends PlaybackWebTest {

    private PlayValidator play;
    private SeekValidator seek;
    private EventValidator eventValidator;
    private ReplayValidator replayValidator;

    public PlaybackReplayVideoTests() throws OoyalaException {
        super();
    }

    @Test(groups = "playerFeatures", dataProvider = "testUrls")
    public void testVideoReplay(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;

        try {
            driver.get(url.getUrl());

            result = result && play.waitForPage();

            injectScript();

            result = result && play.validate("playing_1", 60000);

            result = result && eventValidator.playVideoForSometime(2);

            result = result && seek.validate("seeked_1", 60000);

            result = result && eventValidator.validate("played_1", 20000);

			result = result && replayValidator.validate("replay_1", 30000);
			
			result = result && eventValidator.validate("playing_3", 60000);
			
			result = result && eventValidator.waitOnElement("PLAYING_SCREEN", 60000);
			
            result = result && replayValidator.validatePlayHeadTime();

        } catch (Exception e) {
            logger.error(e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Replay tests failed");
    }
}
