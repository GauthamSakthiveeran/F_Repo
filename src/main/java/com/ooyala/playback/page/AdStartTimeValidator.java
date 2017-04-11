package com.ooyala.playback.page;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by jitendra on 10/4/17.
 */
public class AdStartTimeValidator extends PlayBackPage implements PlaybackValidator{

    public static Logger logger = Logger.getLogger(AdStartTimeValidator.class);

    public AdStartTimeValidator(WebDriver webDriver) {
        super(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @Override
    public boolean validate(String element, int timeout) throws Exception {
        return false;
    }

    public boolean validateAdStartTime(String adStartTime,String adEventLocator){

        int adTime = Integer.parseInt(adStartTime);

        waitOnElement(By.id("adStartTime"),40000);

        int time = Integer.parseInt(driver.findElement(By.id("adStartTime")).getText());

        logger.info("Ad start time is :"+time);

        if (!(time>=adTime && time<=(adTime+3))){
            logger.error("Ad is not starting at "+adTime+" instead it gets started at "+time);
            return false;
        }

        if (!waitOnElement(By.id(adEventLocator),1000)){
            logger.error(adEventLocator + "element is not found or midroll ad is not playing at "+time);
            extentTest.log(LogStatus.FAIL,adEventLocator + "element is not found or midroll ad is not playing at "+time);
            return false;
        }

        logger.info(adEventLocator + "midroll ad played at "+time);
        extentTest.log(LogStatus.FAIL,adEventLocator + "midroll ad played at "+time);
        return true;
    }
}
