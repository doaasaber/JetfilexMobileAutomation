package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import test.utils.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HomePage {
    AppiumDriver driver;
    By firstMovieName = By.xpath("//android.widget.TextView[@text='Venom: The Last Dance']");
    By firstMovieImage=By.xpath("//android.view.View[@content-desc=\"Poster Image of Venom: The Last Dance\"]");
    By filter= By.xpath("//android.view.View/android.view.View/android.view.View[3]/android.widget.Button");
    By releaseDateButton=By.xpath("//android.view.View[4]/android.widget.RadioButton");
    By homePage = By.xpath("//android.view.View[@content-desc=\"Close\"]");
    By firstDate = By.xpath ("//android.view.View[1]/android.widget.TextView[2]");
    By SecondDate = By.xpath ("//android.view.View[1]/android.widget.TextView[2]");
    By ThirdDate = By.xpath ("//android.view.View[1]/android.widget.TextView[2]");


    public  Movie movie;

    public HomePage (AppiumDriver driver){
        this.driver = driver;
         movie=new Movie(driver);

    }
    public void waitUntilWebElementIsPresent(){
        Actions.waitUntilWebElementIsPresent(firstMovieImage,driver);
    }
    public String getMovieName() throws Exception {
        return  Actions.waitAndGetElementText(firstMovieName,driver);
    }

    public Movie clickOnFirstImage() throws Exception {
        Actions.waitAndClickOnWebElement(firstMovieImage,driver);
        return new Movie(driver);
    }

    public void clickOnFilter() throws Exception {
        Actions.waitAndClickOnWebElement(filter,driver);

    }

    public void clickOnReleaseDateButton() throws Exception {
        Actions.waitAndClickOnWebElement(releaseDateButton,driver);

    }
    public void clickOnHomePage() throws Exception {
        Actions.waitAndClickOnWebElement(homePage,driver);

    }
    public LocalDate getFirstDate() throws Exception {
        String date= Actions.waitAndGetElementText(firstDate,driver);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(date, formatter);
        return date1;

    }

    public LocalDate getTodayDate(){
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
        return utcNow.toLocalDate();


    }
}
