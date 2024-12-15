package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import test.utils.*;

public class Movie {
    AppiumDriver driver;
    public Movie(AppiumDriver driver){
        this.driver = driver;
    }
    By movieName = By.xpath("//android.widget.TextView[@text='Venom: The Last Dance']");
    public String getMovieName() throws Exception {
        return  Actions.waitAndGetElementText(movieName,driver);
    }


}
