package com.ooyala.playback.apps.ios.advancedplaybacksampleapp;

import org.apache.log4j.Logger;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.CCAction;
import com.ooyala.playback.apps.actions.ClickAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

public class AdvancedSampleAppUtils extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(AdvancedSampleAppUtils.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private CCAction ccAction;
	private ClickAction clickAction;

	public boolean performAssetSpecificTest(TestParameters test) throws Exception {
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		ccAction = pageFactory.getCcAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickAction = pageFactory.getClickAction();

		try {
			result = result && selectVideo.startAction(test.getAsset());
			if (test.getAsset().contains("CUSTOM_OVERLAY"))
				result = result && clickAction.waitOnElement("OVERLAY", 15000);
			result = result && elementValidator.handleLoadingSpinner();

			if (test.getAsset().contains("PLUGIN")) {
				result = adEventValidator();
			}

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);

			if (test.getAsset().contains("CHANGE_VIDEO")) {
				result = result && selectVideo.letVideoPlayForSec(2);
				result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				result = result && clickAction.startAction("PLAY_VIDEO_1");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
				result = result && clickAction.startAction("PLAY_VIDEO_2");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
				result = result && selectVideo.letVideoPlayForSec(2);
			}

			if (test.getAsset().contains("INSERT_AD") || test.getAsset().contains("PLUGIN")
			        || test.getAsset().contains("MID_ROLL")) {
				if (test.getAsset().contains("INSERT_AD"))
					result = result && clickAction.startAction("INSERT_VAST_AD");
				result = adEventValidator();
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);
				if (test.getAsset().contains("INSERT_AD")) {
					result = result && clickAction.startAction("INSERT_OOYALA_AD");
					result = adEventValidator();
				}
			}
			
			if (test.getAsset().contains("LANGUAGES")) {
				result = result && ccAction.enableCC(); // Default English
				result = result && notificationEventValidator.verifyEvent(Events.CC_ENABLED, 15000);
			}

			if (!test.getAsset().contains("CUSTOM_CONTROLS")) {
				result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				if (test.getAsset().contains("UNBUNDLED_HLS"))
					result = result && seekActionEventValidator(true);
				else
					result = result && seekActionEventValidator(false);
				result = result && pauseAction.startAction("PLAY_PAUSE_BUTTON");
			}

			if (test.getAsset().contains("CUSTOM_CONTROLS")) {
				result = result && selectVideo.letVideoPlayForSec(2);
				result = result && clickAction.startAction("CUSTOM_PLAY_PAUSE_BUTTON");
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				result = result && clickAction.startAction("CUSTOM_PLAY_PAUSE_BUTTON");
			}

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);

			if (test.getAsset().contains("PLUGIN"))
				result = adEventValidator();

			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	private boolean adEventValidator() {
		boolean result = true;
		result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
		result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
		return result;
	}

	private boolean seekActionEventValidator(boolean isSeekForward) {
		boolean result = true;
		try {
			if (isSeekForward)
				result = result && seekAction.setSlider("SLIDER").seekforward().startAction("SEEK_BAR"); // SeekForward
			else
				result = result && seekAction.setSlider("SLIDER").startAction("SEEK_BAR"); // SeekBack
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
		} catch (Exception e) {
			logger.error("Here is an exception" + e);
			result = false;
		}
		return result;

	}

}