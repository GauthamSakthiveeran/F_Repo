package com.ooyala.playback.apps.actions;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ooyala.playback.apps.PlaybackApps;
import com.ooyala.playback.apps.utils.CommandLineParameters;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

/**
 * Created by Gautham
 */
public class ShareAction extends PlaybackApps implements Actions {

    private Logger logger = Logger.getLogger(ShareAction.class);
    public int[] p = new int[2];

    public ShareAction(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        addElementToPageElements("share");
    }

    // Function to click Share button

    @Override
    public boolean startAction(String element) throws Exception {
        // TODO Auto-generated method stub
        Boolean result = true;
        if(getPlatform().equalsIgnoreCase("android"))
        {
        try {
            if (((AndroidDriver) driver).currentActivity()
                    .equals("com.ooyala.sample.players.OoyalaSkinPlayerActivity")) {

                result = result && tapAndwaitForElementAndClick("MOREOPTIONS_BUTTON_ANDROID");

                result = result && waitForElementAndClick(element);

                return result;
            } else {
                extentTest.log(LogStatus.FAIL, "Player Screen not displayed");
                logger.info("Player Screen not displayed");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Clicking Share Button");
            logger.info("Exception While Clicking Share Button");
            return false;

        }
        }
        else
        {
            //Code for IOS
            return true;
        }

    }

    // Function to close Discovery Screen and MoreOptions Screen
    public boolean closeShareScreen(String element) throws Exception {

        Boolean result = true;

        if(getPlatform().equalsIgnoreCase("android"))
        {
        try

        {
        	gotoBack();

            result = result && waitForElementAndClick("MOREOPTIONSSCREEN_CLOSEBUTTON_ANDROID");

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Closing share Screen");
            logger.info("Exception While Closing Share Screen");
            return false;

        }
        }
        else
        {
            //Code for IOS
            return true;
        }

    }

    public boolean closeMoreOptionsScreen() throws Exception {


        if(getPlatform().equalsIgnoreCase("android"))
        {
        try

        {

            return waitForElementAndClick("MOREOPTIONSSCREEN_CLOSEBUTTON_ANDROID");

            

        } catch (Exception e) {
            e.printStackTrace();
            extentTest.log(LogStatus.FAIL, "Exception While Closing More Options Screen");
            logger.info("Exception While Closing More Otions Screen");
            return false;

        }
        }
        else
        {
            //Code for IOS
            return true;
        }

    }
    
    
    public boolean waitForElementAndClick(String element) {
        try {

            if (waitOnElement(element, 5000)) {
                WebElement elementToFind = getWebElement(element);
                elementToFind.click();
                logger.info("Clicked Element");
                extentTest.log(LogStatus.INFO, "Clicked Element");
                return true;
            } else {
            	logger.info("Failed to Click Element");
                extentTest.log(LogStatus.FAIL, "Element not found");
                return false;

            }

        } catch (Exception e) {

            e.printStackTrace();
            logger.info("Failed to Click Element");
            extentTest.log(LogStatus.FAIL, "Failed to Click Element");
            return false;

        }
    }

    public boolean tapAndwaitForElementAndClick(String element) {
        try {

    		if(!clickPauseButton())
    				return false;
        	
    		if (waitOnElement(element, 5000)) {
                WebElement elementToFind = getWebElement(element);
                elementToFind.click();
    			//if(clickOnIndependentElement(element)){
                logger.info("Clicked Element");
                extentTest.log(LogStatus.PASS, "Clicked Element");
                return true;
            } else {
            	tapinMiddleOfPlayerScreen();
            	if(clickOnIndependentElement(element)){
                logger.info("Clicked Element");
                extentTest.log(LogStatus.PASS, "Clicked Element");
                return true;
                }
                else
                {
                 logger.info("Failed to Click Element");
                 extentTest.log(LogStatus.FAIL, "Failed to Click Element");
                 return false;	
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
            logger.info("Failed to Click Element");
            extentTest.log(LogStatus.FAIL, "Failed to Click Element");
            return false;

        }
    }

    private void tapinMiddleOfPlayerScreen() {

        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        logger.info("Failed to Click Element");
        extentTest.log(LogStatus.INFO, "Clicked Element");

    }

    private void doubletapinMiddleofPlayerScreen() {
        try
        {
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        Thread.sleep(2000);
        touch.tap((size.getWidth()) / 2, (size.getHeight() / 2)).perform();
        logger.info("Double Tap Done");
        extentTest.log(LogStatus.INFO, "Double Tap Done");
        }
        catch(Exception e)
        {
            logger.info("Failed to Click Double Tap ");
            extentTest.log(LogStatus.FAIL, "Failed to Click Double Tap");  
        }

    }
    
    public boolean clickPlayButton()
    {
    	try
    	{
 
    	Thread.sleep(3000);
		WebElement playPauseButton = getWebElement("PLAY_PAUSE_BUTTON_ANDROID");
		p[0] = playPauseButton.getLocation().getX() + playPauseButton.getSize().getWidth()/2;
		p[1] = playPauseButton.getLocation().getY() + playPauseButton.getSize().getHeight()/2;
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        Dimension size = driver.manage().window().getSize();
        touch.tap(p[0],p[1]).perform();
        logger.info("Clicked Play Button");
        extentTest.log(LogStatus.INFO, "Clicked Play Button");
        return true;
    	}
    	catch (Exception e)
    	{
            logger.info("Failed to Click Play Button");
            extentTest.log(LogStatus.FAIL, "Failed to Click Play Button");
    	return false;
    	}
		
    }
    public boolean clickPauseButton()
    {
    	try
    	{
    	Thread.sleep(3000);
        TouchAction touch = new TouchAction((AppiumDriver) driver);
        touch.tap(p[0],p[1]).perform();
        Thread.sleep(2000);
        touch.tap(p[0],p[1]).perform();
        logger.info("Clicked Pause Button");
        extentTest.log(LogStatus.INFO, "Clicked Pause Button");
        return true;
    	}
        catch(Exception e){
            logger.info("Failed to Clicked Pause Button");
            extentTest.log(LogStatus.FAIL, "Failed to Clicked Pause Button");
        	return false;
    		
    	}
		
    }
    
	
}
