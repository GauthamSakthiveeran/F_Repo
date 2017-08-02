package com.ooyala.playback.amf;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ooyala.playback.PlaybackWebTest;
import com.ooyala.playback.page.DifferentElementValidator;
import com.ooyala.playback.page.EventValidator;
import com.ooyala.playback.page.PlayValidator;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.url.UrlObject;
import com.ooyala.qe.common.exception.OoyalaException;
import com.relevantcodes.extentreports.LogStatus;

/**
 * Created by jitendra on 2/3/17.
 */
public class PlaybackVerifyVideoElementCreatedAllPreMiPostAdsTests extends PlaybackWebTest {

    private static Logger logger = Logger.getLogger(PlaybackVerifyVideoElementCreatedAllPreMiPostAdsTests.class);
    private EventValidator eventValidator;
    private PlayValidator play;
    private PlayAction playAction;
    private DifferentElementValidator differentElementValidator;
    private SeekAction seekAction;

    PlaybackVerifyVideoElementCreatedAllPreMiPostAdsTests() throws OoyalaException {
        super();
    }

    @Test(groups = "VideoCreated", dataProvider = "testUrls")
    public void testVideoElementCreated(String testName, UrlObject url)
            throws OoyalaException {

        boolean result = true;
        try {
            driver.get(url.getUrl());

            result=result && play.waitForPage();

            injectScript();

			result = result && playAction.startAction();

            result = result && eventValidator.validate("PreRoll_willPlayAds", 30000);

            result = result && eventValidator.validate("adsPlayed_1", 300000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                result = result && eventValidator.validateVideoElementOccuredCount(40000);
            }

            result = result && eventValidator.validate("playing_1", 90000);


            if (!eventValidator.validate("MidRoll_willPlayAds", 10000)) {
                result = result && seekAction.setFactor(2).fromLast().setTime(10).startAction();
            }

            result = result && eventValidator.validate("MidRoll_willPlayAds", 200000);

            result = result && eventValidator.validate("adsPlayed_2", 600000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                result = result && eventValidator.validateVideoElementOccuredCount(40000);
            }

            /*if (!eventValidator.validate("played_1", 4000)) {
                result = result && seekAction.seekTillEnd().startAction();
            }*/

            result = result && eventValidator.validate("PostRoll_willPlayAds", 200000);
            result = result && eventValidator.validate("adsPlayed_3", 600000);

            // for IMA ad videoControllerVideoElementCreated event is not triggering
            if (!eventValidator.isAdPluginPresent("ima")) {
                result = result && eventValidator.validateVideoElementOccuredCount(40000);
            }

            result = result && eventValidator.validate("played_1", 180000);
            result = result && differentElementValidator.validateMainVideoElementId("VIDEO_ELEMENT",20000);

            if (!eventValidator.isAdPluginPresent("ima") ){
                result = result && differentElementValidator.validateAdElementId("AD_ELEMENT",5000);
            }

        } catch (Exception e) {
            logger.error("Exception while checking Video Element tests  "+e.getMessage());
            extentTest.log(LogStatus.FAIL, e.getMessage());
            result = false;
        }
        Assert.assertTrue(result, "Playback Video Element test failed");

    }
}
