package com.reine.filebed.fxml;

import com.reine.filebed.LocalFilebedApplication;
import com.reine.filebed.service.FileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

import java.io.File;

/**
 * @author reine
 * 2022/6/30 7:51
 */
public class Main {

    @FXML
    private TextField tfProject;

    @FXML
    private Label lbInfo;

    @FXML
    private ImageView ivImage;

    private FileService fileService;
    private File file;

    @FXML
    void boxDragOver(DragEvent event) {
        event.acceptTransferModes(event.getTransferMode());
    }

    @FXML
    void boxDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasUrl()) {
            String url = dragboard.getUrl();
            file = new File(url);
            Image image = new Image(url);
            ivImage.setPreserveRatio(true);
            // 设置不超过图片展示区域
            if (image.getWidth() > image.getHeight()) {
                ivImage.setFitWidth(400);
            } else {
                ivImage.setFitHeight(250);
            }
            ivImage.setImage(image);
        }
    }

    @FXML
    void uploadFile(ActionEvent event) {
        String projectText = tfProject.getText();
        fileService = (FileService) LocalFilebedApplication.APPLICATION_CONTEXT.getBean("fileServiceImpl");
        String s = fileService.storeImage(projectText, file);
        if (s != null) {
            lbInfo.setText("http://localhost:8824" + s);
        }
    }
}
