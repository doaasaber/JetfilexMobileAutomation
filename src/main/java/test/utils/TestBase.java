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
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;

public class TestBase {
    private static AppiumDriverLocalService server;
    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<Properties> props = new ThreadLocal<>();

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

    private boolean isAppiumServerRunning(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private AppiumDriverLocalService startAppiumServer() {
//        if (server == null || !server.isRunning()) {
//            String userName = System.getProperty("user.name");
//            HashMap<String, String> environment = new HashMap<>();
//            environment.put("PATH", "/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin:/Users/" + userName + "/Library/Android/sdk/tools:/Users/" + userName + "/Library/Android/sdk/platform-tools:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/Library/Apple/usr/bin" + System.getenv("PATH"));
//            environment.put("ANDROID_HOME", "/Users/" + userName + "/Library/Android/sdk");

            AppiumServiceBuilder builder = new AppiumServiceBuilder();
            builder.withIPAddress("127.0.0.1");
            builder.usingAnyFreePort();
            builder.withLogOutput(System.out);
            builder.withTimeout(Duration.ofMinutes(1));
            builder.withArgument(() -> "--allow-insecure=adb_shell");

            server = AppiumDriverLocalService.buildService(builder);
            server.clearOutPutStreams();
            server.start();
            if (server == null || !server.isRunning()) {
                throw new RuntimeException("An appium server node is not started!");
            }

            server.start();
            log("Appium server started at: " + server.getUrl());
        //}
        return server;
    }

    @BeforeTest
    public void beforeTest() throws Exception {
        URL url;
        InputStream inputStream = null;
        Properties props = new Properties();

        try {
            // Load properties
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            setProps(props);

            // Start Appium server
            if (!isAppiumServerRunning(4723)) {
                startAppiumServer();
            }

            // Set capabilities
            UiAutomator2Options desiredCapabilities = new UiAutomator2Options();
            desiredCapabilities.setPlatformName(props.getProperty("platformName"));
            desiredCapabilities.setDeviceName(props.getProperty("deviceName"));

            switch (props.getProperty("platformName")) {
                case "Android":
                    desiredCapabilities.setAutomationName(props.getProperty("androidAutomationName"));
                    desiredCapabilities.setAppPackage(props.getProperty("androidAppPackage"));
                    desiredCapabilities.setAppActivity(props.getProperty("androidAppActivity"));
                    desiredCapabilities.setApp("src/test/java/resources/app/app-debug.apk");

                    driver.set(new AndroidDriver(server.getUrl(), desiredCapabilities));
                    break;

                case "iOS":
                    desiredCapabilities.setAutomationName(props.getProperty("iOSAutomationName"));
                    //desiredCapabilities.setBundleId(props.getProperty("iOSBundleId"));
                    desiredCapabilities.setApp("/path/to/your/ios/app");

                    driver.set(new IOSDriver(server.getUrl(), desiredCapabilities));
                    break;

                default:
                    throw new Exception("Invalid platform name: " + props.getProperty("platformName"));
            }

            log("Driver initialized: " + driver.get());
        } catch (Exception e) {
            log("Error initializing driver: " + e.getMessage());
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (driver.get() != null) {
            driver.get().quit();
            log("Driver quit");
        }

        if (server != null && server.isRunning()) {
            server.stop();
            log("Appium server stopped");
        }
    }

    public void log(String message) {
        System.out.println(message);
    }
}
