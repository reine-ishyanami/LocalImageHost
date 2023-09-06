package com.reine.imagehost.ui;

import com.reine.imagehost.LocalImageHostApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

/**
 * @author reine
 */
public class AppUI extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void start(Stage stage) {
        URL fxmlUrl = getClass().getResource("/fxml/main.fxml");
        URL cssUrl = getClass().getResource("/css/main.css");
        URL iconUrl = getClass().getResource("/image/logo.png");
        String title = "上传图片";
        EventProperty eventProperty = new EventProperty(stage, fxmlUrl, iconUrl, cssUrl, title);
        applicationContext.publishEvent(new StageReadyEvent(eventProperty));
    }


    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(LocalImageHostApplication.class).run();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
