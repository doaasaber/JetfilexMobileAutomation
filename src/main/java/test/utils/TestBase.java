package test.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

public class TestBase {
    private static AppiumDriverLocalService server;
    protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal <Properties> props = new ThreadLocal<>();


    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    public Properties getProps() {
        return props.get();
    }

    public void setProps(Properties props2) {
        props.set(props2);
    }

    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    public AppiumDriverLocalService getAppiumService() {
        String userName=System.getProperty("user.name");
        HashMap<String, String> environment = new HashMap<String, String>();
        environment.put("PATH", "/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin:/Users/"+userName+"/Library/Android/sdk/tools:/Users/"+userName+"/Library/Android/sdk/platform-tools:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin" + System.getenv("PATH"));
        environment.put("ANDROID_HOME", "/Users/"+userName+"/Library/Android/sdk");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .usingAnyFreePort()
//                .withArgument(()-> "--base-path", "/wd/hub")
                .withArgument(() -> "--use-plugins","relaxed-caps")
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withEnvironment(environment)
                .withLogFile(new File("AppiumServerLog" + File.separator + "Server.log")));
    }

    public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
        boolean isAppiumServerRunning = false;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            socket.close();
        } catch (IOException e) {
            System.out.println("1");
            isAppiumServerRunning = true;
        } finally {
            socket = null;
        }
        return isAppiumServerRunning;
    }

    @BeforeTest
    public void beforeTest() throws Exception {
        URL url;
        InputStream inputStream = null;
        InputStream stringsis = null;
        Properties props = new Properties();
        AppiumDriver driver;


        try {
            props = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            setProps(props);


            UiAutomator2Options desiredCapabilities = new UiAutomator2Options();
            desiredCapabilities.setCapability("platformName",props.getProperty("platformName") );
            desiredCapabilities.setCapability("platformVersion",props.getProperty("platformVersion") );
            desiredCapabilities.setCapability("deviceName", props.getProperty("deviceName"));
            desiredCapabilities.setCapability("udid", props.getProperty("udid"));
            url = new URL(props.getProperty("appiumURL"));

            switch(props.getProperty("platformName")) {
                case "Android":
                    desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));
                    desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));

                    desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
                    desiredCapabilities.setCapability("newCommandTimeout", 10000);

                    	//String androidAppUrl =System.getProperty("user.dir") + props.getProperty("androidAppLocation");
                   // String androidAppUrl = System.getProperty("user.dir") + File.separator + props.getProperty("androidAppLocation");
                    String androidAppUrl ="C:/Users/AbdElGhanyD/IdeaProjects/Jetflix/src/test/java/resources/app/app-debug.apk";
                            log("appUrl is" + androidAppUrl);
                    desiredCapabilities.setCapability("app", androidAppUrl);

                    driver = new AndroidDriver(url, desiredCapabilities);
                    break;
                case "iOS":
                    desiredCapabilities.setCapability("automationName", props.getProperty("iOSAutomationName"));
                    String iOSAppUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                            + File.separator + "resources" + File.separator + "app" + File.separator + "SwagLabsMobileApp.resources.app";
                    //	String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
                    log("appUrl is" + iOSAppUrl);
                    desiredCapabilities.setCapability("bundleId", props.getProperty("iOSBundleId"));

                    desiredCapabilities.setCapability("app", iOSAppUrl);

                    driver = new IOSDriver(url, desiredCapabilities);
                    break;
                default:
                    throw new Exception("Invalid platform! - " + props.getProperty("platformName"));
            }
            setDriver(driver);
            log("driver initialized: " + driver);
        } catch (Exception e) {
            log("driver initialization failure. ABORT!!!\n" + e.toString());
            throw e;
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            if(stringsis != null) {
                stringsis.close();
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if(server.isRunning()){
            server.stop();
            log("Appium server stopped");
        }
    }

    public void log(String mess) {
          System.out.println(mess);
    }

}
