package com.reine.imagehost.ui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author reine
 */
@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("${spring.application.ui.view}")
    private Resource view;

    @Value("${spring.application.ui.icon}")
    private Resource icon;

    @Value("${spring.application.ui.css}")
    private Resource css;

    @Value("${spring.application.ui.title}")
    private String applicationTitle;

    private final ApplicationContext applicationContext;

    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(view.getURL());
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            root.getStylesheets().add(css.getURL().toString());
            Stage stage = event.getStage();
            stage.setTitle(applicationTitle);
            stage.getIcons().add(new Image(icon.getURL().toString()));
            stage.setScene(new Scene(root));
            stage.show();
            // 最大化
            stage.maximizedProperty().addListener((observable, oldValue, newValue) -> stage.setFullScreen(newValue));
            // 全屏
            stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> stage.setMaximized(newValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
