package com.ooyala.playback.apps.actions;

import com.ooyala.playback.apps.PlaybackApps;
import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

public class PauseAction extends PlaybackApps implements Actions {

    private static Logger logger = Logger.getLogger(PlayAction.class);

	@Override
	public boolean startAction(String element) throws Exception {
		try {
			tapScreenIfRequired();
			if(!clickOnIndependentElement(element)) {
				logger.error("Unable to click on play pause.");
				return false;
			}
		} catch (Exception e) {
			logger.info("Play button not found. Tapping screen and retrying..");
			tapScreenIfRequired();
			if(!clickOnIndependentElement(element)) {
				logger.error("Unable to click on play pause.");
				return false;
			}
		}
		return true;
	}

    public PauseAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("playpause");
    }

    /*@Override
    public boolean startAction(String element) throws Exception {
        try {
            if(!getPlayPause(element)) {
                logger.error("Unable to get the element");
            }
        } catch (Exception e) {
            logger.info("Play button not found. Tapping screen and retrying..");
            tapOnScreen();
            if(!getPause()) {
                logger.error("Unable to click>>>>>>>>>>>> on pause.");
                return false;
            }
        }
        return true;
    }*/
}
