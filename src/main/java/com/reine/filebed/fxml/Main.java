package com.reine.filebed.fxml;

import com.reine.filebed.LocalFilebedApplication;
import com.reine.filebed.service.FileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * @author reine
 * 2022/6/30 7:51
 */
public class Main {

    @FXML
    private TextField tfProject;

    @FXML
    private TextField tfInfo;

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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: #ff9999; -fx-border-width: 3;");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("错误");
        if (projectText.trim().equals("")) {
            alert.setContentText("项目名不能为空");
            alert.show();
            return;
        } else if (file == null) {
            alert.setContentText("图片不能为空");
            alert.show();
            return;
        }
        fileService = (FileService) LocalFilebedApplication.APPLICATION_CONTEXT.getBean("fileServiceImpl");
        String s = fileService.storeImage(projectText, file);
        if (s != null) {
            tfInfo.setVisible(true);
            tfInfo.setText("http://localhost:8824/view" + s);
        }
    }

}
