package com.reine.imagehost.fxcontroller;

import com.reine.imagehost.service.FileService;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.stage.*;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author reine
 * 2022/6/30 7:51
 */
@FXMLController
public class Main implements Initializable {

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
    @Resource
    private FileService fileService;

    @Value("${local.store}")
    private String originPath;

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
            setIvImage(url);
        }
    }

    @FXML
    void uploadFile() throws Exception {
        String projectText = tfProject.getText();
        if (emptyAlert(projectText)) {
            return;
        }
        String path = tfPath.getText();
        Map<String, String> resultMap = fileService.storeImageGUI(path, projectText, file);
        if (resultMap != null) {
            tfInfo.setVisible(true);
            String project = resultMap.get("project");
            String fileName = resultMap.get("filename");
            tfInfo.setText("http://localhost:8824/" + project + "/" + fileName);
        }
    }

    private boolean emptyAlert(String projectText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: #ff9999; -fx-border-width: 3;");
        alert.initStyle(StageStyle.UNDECORATED);
        Window window = tfInfo.getScene().getWindow();
        alert.initOwner(window);
        alert.setHeaderText("错误");
        if ("".equals(projectText.trim())) {
            alert.setContentText("项目名不能为空");
            alert.show();
            return true;
        } else if (file == null) {
            alert.setContentText("图片不能为空");
            alert.show();
            return true;
        }
        return false;
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

    @FXML
    void selectImage() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("图片", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        Optional.ofNullable(selectedFile).ifPresent(file1 -> {
            file = new File("file:\\" + file1.getPath());
            String url = file.getPath();
            setIvImage(url);
        });
    }

    private void setIvImage(String url) {
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

    /**
     * GUI数据初始化
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileService.createTable();
        if (originPath != null) {
            path = new File(originPath);
            tfPath.setText(path.getAbsolutePath());
        }
    }
}
