package test.utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.IOException;
import java.util.Properties;

public class DriverSingleton extends TestBase {
    private static final String PLATFORM_NAME = "platformName";
    public static final String PLATFORM_VERSION = "platformVersion";
    public static final String DEVICE_NAME = "deviceName";
    public static final String AUTOMATION_NAME ="automationName";
    private static Properties properties;
    private static UiAutomator2Options options;
    private static DriverSingleton driverSingleton = null;
    private AndroidDriver driver;

    private DriverSingleton(){
        setAndroidDriver();
    }

    public static DriverSingleton getDriverSingleton() {
        if (driverSingleton == null)
            driverSingleton = new DriverSingleton();
        return driverSingleton;
    }

    public AndroidDriver getDriver() {
        return driver;
    }

    public void setAndroidDriver() {
        try {
            ConfigHandler.setAndroidProperties();
            properties = ConfigHandler.getAndroidProperties();
            setUiAutomator2Options();
            setAppPackageAndActivity();

          //  driver = new AndroidDriver(server.getCurrentServerUrl(), options);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void setUiAutomator2Options() throws IOException {

        options = new UiAutomator2Options()
                .setPlatformName(properties.getProperty(PLATFORM_NAME))
                .setPlatformVersion(properties.getProperty(PLATFORM_VERSION))
                .setDeviceName(properties.getProperty(DEVICE_NAME))
                .setAutomationName(properties.getProperty(AUTOMATION_NAME));
        options.setCapability("newCommandTimeout", 1200);
        options.setCapability("autoDismissAlerts", true);
        options.setCapability("unicodeKeyboard", true);
        options.setCapability("resetKeyboard", true);

    }

    private static void setAppPackageAndActivity() {
        options.setAppPackage(properties.getProperty("appPackage"));
        options.setAppActivity(properties.getProperty("appActivity"));
    }
}
