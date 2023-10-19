package com.reine.imagehost.ui;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
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
            Stage stage = Optional.ofNullable(property.stage()).orElse(mainStage);
            // 如果主窗口未设置，则将该窗口第一个启动的窗口设置为主窗口
            if (this.mainStage == null) this.mainStage = stage;
            // 如果传入的窗口不是已有的主窗口，则将主窗口设置为其父窗口
            if (!stage.equals(mainStage)) {
                stage.initOwner(mainStage);
                // 设置模态窗口
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            stage.getIcons().add(
                    new Image(Optional.ofNullable(property.iconUrl()).orElse(defaultIcon.getURL()).toString())
            );
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
