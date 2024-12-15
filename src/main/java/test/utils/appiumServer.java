package test.utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.net.URL;
import java.time.Duration;

public class appiumServer {
    AppiumDriverLocalService service;

    public appiumServer() {

        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingAnyFreePort();
        builder.withLogOutput(System.out);
        builder.withTimeout(Duration.ofMinutes(1));
        builder.withArgument(() -> "--allow-insecure=adb_shell");

        service = AppiumDriverLocalService.buildService(builder);
        service.clearOutPutStreams();
        service.start();
        if (service == null || !service.isRunning()) {
            throw new RuntimeException("An appium server node is not started!");
        }
    }

    public URL getCurrentServerUrl() {
        return service.getUrl();
    }

    public void closeAppiumServer() {
        if (service.isRunning()) {
            service.stop();
        }
    }
}
