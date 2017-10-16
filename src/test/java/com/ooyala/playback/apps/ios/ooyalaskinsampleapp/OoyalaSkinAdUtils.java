package com.ooyala.playback.apps.ios.ooyalaskinsampleapp;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.ooyala.playback.PlaybackAppsTest;
import com.ooyala.playback.apps.TestParameters;
import com.ooyala.playback.apps.actions.ClickAction;
import com.ooyala.playback.apps.actions.PauseAction;
import com.ooyala.playback.apps.actions.PlayAction;
import com.ooyala.playback.apps.actions.QAModeSwitchAction;
import com.ooyala.playback.apps.actions.SeekAction;
import com.ooyala.playback.apps.actions.SelectVideoAction;
import com.ooyala.playback.apps.actions.SwipeUpDownAppAssetsAction;
import com.ooyala.playback.apps.validators.ElementValidator;
import com.ooyala.playback.apps.validators.Events;
import com.ooyala.playback.apps.validators.NotificationEventValidator;

public class OoyalaSkinAdUtils extends PlaybackAppsTest {

	private static Logger logger = Logger.getLogger(OoyalaSkinAdUtils.class);
	private SelectVideoAction selectVideo;
	private ElementValidator elementValidator;
	private NotificationEventValidator notificationEventValidator;
	private QAModeSwitchAction qaModeSwitchAction;
	private PlayAction playAction;
	private PauseAction pauseAction;
	private SeekAction seekAction;
	private ClickAction clickAction;
	private SwipeUpDownAppAssetsAction swipeAppAssetAction;
	private int noOfAds = 0;
	private String iosPlayPause;
	private String iosSlider;
	private String iosSeekBar;

	public boolean performAssetSpecificTest(TestParameters test) throws Exception {
		Reporter.log("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
		logger.info("Executing:" + test.getApp() + "->Asset:" + test.getAsset());
		boolean result = true;
		notificationEventValidator = pageFactory.getNotificationEventValidator();
		pauseAction = pageFactory.getPauseAction();
		seekAction = pageFactory.getSeekAction();
		elementValidator = pageFactory.getEventValidator();
		selectVideo = pageFactory.getSelectVideoAction();
		clickAction = pageFactory.getClickAction();
		qaModeSwitchAction = pageFactory.getQAModeSwitchAction();
		playAction = pageFactory.getPlayAction();
		swipeAppAssetAction = pageFactory.getSwipeUpDownAppAssetsAction();
		
		boolean islargeAsset = test.getDescription().contains("large");
		int seekCount=islargeAsset?2:1;
		
		iosPlayPause="PLAY_PAUSE_BUTTON_V4_IOS";
		iosSlider="SLIDER_V4";
		iosSeekBar="SEEK_BAR_V4";
		boolean performVastSkip=test.getAsset().contains("VMAP")?true:false;
		performVastSkip=test.getAsset().contains("VAST_3_Overlay_PreMid")?true:performVastSkip;
		selectVideo.isAppV4(test.getApp());

		
		try {
			result = result && selectVideo.startAction("BASIC_PLAYBACK");
			result = result && qaModeSwitchAction.startAction("QA_MODE_SWITCH");
			result = result && selectAsset(test.getAsset());
			result = result && elementValidator.validate("NOTIFICATION_AREA", 1000);
			result = result && elementValidator.handleLoadingSpinner();
			result = result && playAction.startAction("PLAY_BUTTON_V4_IOS");

			if(test.getAsset().contains("PODDED")){
				setNoOfAds(test.getDescription().split(" ")[0]);
			}
		
			if (test.getAsset().contains("PRE")) {
				for (int i = 0; i < noOfAds; i++) {
					result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
					if(test.getAsset().contains("ICON")){
						result = result && elementValidator.validate("VAST_ICON", 10000);
					}
					if(performVastSkip){
						elementValidator.validate("SKIP_AD", 10000);
						clickAction.startAction("SKIP_AD");
					}
					result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				}
			}
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_STARTED, 25000);
			
			if(test.getAsset().contains("OVERLAY")){
				result = result && elementValidator.validate("AD_OVERLAY", 20000);
				result = result && elementValidator.validateElementDisappeared("AD_OVERLAY", 30);
			}

			if (test.getAsset().contains("MID")) {
				for (int i = 0; i < noOfAds; i++) {
					result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 25000);
					if(test.getAsset().contains("ICON")){
						result = result && elementValidator.validate("VAST_ICON", 10000);
					}
					if(performVastSkip){
						elementValidator.validate("SKIP_AD", 10000);
						clickAction.startAction("SKIP_AD");
					}
					result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				}
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 25000);
			}
			
			if(islargeAsset)
				playAction.letVideoPlayForSec(3);
			
			
			for(int i=0;i< seekCount;i++){
				result = result && pauseAction.startAction(iosPlayPause);
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_PAUSED, 25000);
				result = result && seekActionEventValidator(true);
				result = result && pauseAction.startAction(iosPlayPause);
				result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_RESUMED, 30000);
				}

			if (test.getAsset().contains("POST")) {
				for (int i = 0; i < noOfAds; i++) {
					result = result && notificationEventValidator.verifyEvent(Events.AD_STARTED, 90000);
					if(test.getAsset().contains("ICON")){
						result = result && elementValidator.validate("VAST_ICON", 10000);
					}
					if(performVastSkip){
						elementValidator.validate("SKIP_AD", 10000);
						clickAction.startAction("SKIP_AD");
					}
					result = result && notificationEventValidator.verifyEvent(Events.AD_COMPLETED, 25000);
				}
			}
			

			
			result = result && notificationEventValidator.verifyEvent(Events.PLAYBACK_COMPLETED, 90000);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Here is an exception" + ex);
			result = false;
		}
		return result;
	}

	private boolean seekActionEventValidator(boolean isSeekForward) {
		boolean result = true;
		try {
			if (isSeekForward)
				result = result && seekAction.setSlider(iosSlider).seekforward().startAction(iosSeekBar); // SeekForward
			else
				result = result && seekAction.setSlider(iosSlider).startAction(iosSeekBar); // SeekBack
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_STARTED, 40000);
			result = result && notificationEventValidator.verifyEvent(Events.SEEK_COMPLETED, 40000);
		} catch (Exception e) {
			logger.error("Here is an exception" + e);
			result = false;
		}
		return result;

	}
	
	public OoyalaSkinAdUtils setNoOfAds(String noOfAds) {
		this.noOfAds = Integer.parseInt(noOfAds);
		return this;
	}
	
	public boolean selectAsset(String asset){
		try {
			if(!selectVideo.startAction(asset)){
				logger.info("Asset not in View, So swiping up");
				swipeAppAssetAction.swipeAssetListIos("VAST_3_PODDED_PREROLL", "HLS");
				if(!selectVideo.startAction(asset)){
					logger.info("Asset not in View, So swiping up");
					swipeAppAssetAction.swipeAssetListIos("VAST_3_ICON_BOTTOM_LEFT_PRE","VAST_3_PODDED_PREROLL");
					if(!selectVideo.startAction(asset)){
						logger.error("Asset not in View, So swiping up");
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;	
	}
}