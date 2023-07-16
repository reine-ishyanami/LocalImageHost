package com.reine.imagehost.ui;

import com.reine.imagehost.LocalImageHostApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author reine
 */
public class AppUI extends Application {
    private ConfigurableApplicationContext applicationContext;


    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
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
