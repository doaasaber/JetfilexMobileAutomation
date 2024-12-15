package test.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Actions {
    public static void waitUntilWebElementIsPresent(By element , WebDriver driver){

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(element));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void waitAndClickOnWebElement(By element, WebDriver driver) throws Exception {
        waitUntilWebElementIsPresent(element,driver);
        try {
            driver.findElement(element).click();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new Exception("Can not click on webElement: " + element.toString(),e);
        }
    }

    public static void waitAndEnterTextInWebElement(String text, By element, WebDriver driver) throws Exception {
        waitUntilWebElementIsPresent(element,driver);
        try {
            driver.findElement(element).clear();
            driver.findElement(element).sendKeys(text);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new Exception("Can not enter text in webElement: " + element.toString(),e);
        }
    }
    public static String waitAndGetElementText(By element, WebDriver driver) throws Exception {
        waitUntilWebElementIsPresent(element,driver);
        String Text;
        try {
            Text=driver.findElement(element).getText();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new Exception("Can not get text of webElement: " + element.toString(),e);
        }
        return Text;
    }

}
