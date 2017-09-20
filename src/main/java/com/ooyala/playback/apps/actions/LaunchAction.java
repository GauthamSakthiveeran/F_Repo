package com.ooyala.playback.apps.actions;

import com.ooyala.playback.apps.PlaybackApps;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;

public class LaunchAction extends PlaybackApps implements Actions {

    private static Logger logger = Logger.getLogger(LaunchAction.class);

    public LaunchAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    @Override
    public boolean startAction(String element) throws Exception {
        return false;
    }

    public boolean LaunchApp() throws Exception {
        driver.launchApp();
        logger.info("Application has been launched successfully");
        extentTest.log(LogStatus.INFO, "Application has been launched successfully");
        return true;
    }


}