package com.reine.imagehost.ui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author reine
 */
@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

//    @Value("${spring.application.ui.view}")
//    private Resource view;
//
//    @Value("${spring.application.ui.icon}")
//    private Resource icon;
//
//    @Value("${spring.application.ui.css}")
//    private Resource css;
//
//    @Value("${spring.application.ui.title}")
//    private String applicationTitle;

    private Stage mainStage = null;

    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            EventProperty property = event.getProperty();
            FXMLLoader loader = new FXMLLoader(property.fxmlUrl());
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            root.getStylesheets().add(property.cssUrl().toString());
            Stage stage = property.stage();
            stage.setTitle(property.title());
            stage.getIcons().add(new Image(property.iconUrl().toString()));
            stage.setScene(new Scene(root));
            stage.show();
            if (this.mainStage == null) this.mainStage = stage;
            // 最大化
            stage.maximizedProperty().addListener((observable, oldValue, newValue) -> stage.setFullScreen(newValue));
            // 全屏
            stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> stage.setMaximized(newValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
