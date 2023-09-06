package com.reine.imagehost.ui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * @author reine
 */
@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("${spring.application.ui.default-css}")
    private Resource defaultCss;

    @Value("${spring.application.ui.default-icon}")
    private Resource defaultIcon;

    @Value("${spring.application.ui.default-title}")
    private String defaultTitle;

    private Stage mainStage = null;

    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            EventProperty property = event.getProperty();
            FXMLLoader loader = new FXMLLoader(property.fxmlUrl());
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            root.getStylesheets().add(Optional.ofNullable(property.cssUrl()).orElse(defaultCss.getURL()).toString());
            Stage stage = property.stage();
            stage.setTitle(Optional.ofNullable(property.title()).orElse(defaultTitle));
            stage.getIcons().add(new Image(Optional.ofNullable(property.iconUrl()).orElse(defaultIcon.getURL()).toString()));
            stage.setScene(new Scene(root));
            stage.show();
            if (this.mainStage == null) this.mainStage = stage;
            // 最大化
//            stage.maximizedProperty().addListener((observable, oldValue, newValue) -> stage.setFullScreen(newValue));
            // 全屏
//            stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> stage.setMaximized(newValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
