package runner;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import test.utils.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestCases {
    AppiumDriver driver;
    TestBase base = new TestBase();
    HomePage homePage;


    @BeforeMethod
    public void beforeTest() throws Exception {
        base.beforeTest();
        driver = base.getDriver();
        ((AndroidDriver)driver).activateApp("com.yasinkacmaz.jetflix.debug");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        homePage=new HomePage(driver);
        homePage.waitUntilWebElementIsPresent();



    }

    @AfterMethod
    public void afterTest() {

        ((InteractsWithApps) driver).terminateApp("com.yasinkacmaz.jetflix.debug");

    }





    @Test(priority = 1)
    public void validateOnMovieName() throws Exception {
        String movieName1= homePage.getMovieName();
        homePage.clickOnFirstImage();
        String movieName2=homePage.movie.getMovieName();
        Assert.assertEquals(movieName1,movieName2);
    }


    @Test(priority = 2)
    public void filterByReleaseDate() throws Exception {
    homePage.clickOnFilter();
    homePage.clickOnReleaseDateButton();
    homePage.clickOnHomePage();
    LocalDate movieDate=homePage.getFirstDate();
    homePage.getTodayDate();
    Assert.assertTrue(movieDate.isAfter(homePage.getTodayDate()));




    }





    }


