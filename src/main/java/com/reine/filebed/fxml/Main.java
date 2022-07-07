package com.reine.filebed.fxml;

import com.reine.filebed.LocalFilebedApplication;
import com.reine.filebed.service.FileService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Optional;

/**
 * @author reine
 * 2022/6/30 7:51
 */
@Configuration
public class Main {

    @FXML
    public TextField tfPath;
    @FXML
    private TextField tfProject;

    @FXML
    private TextField tfInfo;

    @FXML
    private ImageView ivImage;

    /**
     * 图片文件
     */
    private File file;
    /**
     * 文件存储路径
     */
    private File path;
    /**
     * 文件及数据库操作服务
     */
    private FileService fileService;

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
    void uploadFile() throws Exception {
        String projectText = tfProject.getText();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: #ff9999; -fx-border-width: 3;");
        alert.initStyle(StageStyle.UNDECORATED);
        Window window = tfInfo.getScene().getWindow();
        alert.initOwner(window);
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
        String path = tfPath.getText();
        String s = fileService.storeImageGUI(path, projectText, file);
        if (s != null) {
            tfInfo.setVisible(true);
            tfInfo.setText("http://localhost:8824/view" + s);
        }
    }

    @FXML
    void updatePath() {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择文件夹");
        directoryChooser.setInitialDirectory(path);
        File file = directoryChooser.showDialog(stage);
        Optional.ofNullable(file).ifPresent(file1 -> tfPath.setText(file1.getAbsolutePath()));
    }

    /**
     * GUI数据初始化
     */
    public void initData() {
        String originPath = LocalFilebedApplication.APPLICATION_CONTEXT.getEnvironment().getProperty("local.store");
        fileService = (FileService) LocalFilebedApplication.APPLICATION_CONTEXT.getBean("fileServiceImpl");
        fileService.createTable();
        if (originPath != null) {
            path = new File(originPath);
            tfPath.setText(path.getAbsolutePath());
        }
    }
}
