package com.reine.imagehost.ui.controller;

import com.reine.imagehost.service.FileService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author reine
 * 2022/6/30 7:51
 */
@Component
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
    private FileService fileService;

    /**
     * 上传后文件的本地存储位置
     */
    @Value("${local.store}")
    private String originPath;

    /**
     * 应用启动端口
     */
    @Value("${server.port}")
    private String port;

    @Value("${web.base.path.image}")
    private String webBasePath;

    /**
     * 拖拽经过事件
     *
     * @param event
     */
    @FXML
    void boxDragOver(DragEvent event) {
        event.acceptTransferModes(event.getTransferMode());
    }

    /**
     * 拖拽释放事件
     *
     * @param event
     */
    @FXML
    void boxDragDropped(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasUrl()) {
            String url = dragboard.getUrl();
            file = new File(url);
            setIvImage(url);
        }
    }

    /**
     * 上传文件按钮点击事件
     *
     * @throws Exception
     */
    @FXML
    void uploadFile() throws Exception {
        String projectText = tfProject.getText();
        if (emptyAlert(projectText)) {
            return;
        }
        String path = tfPath.getText();
        Map<String, Object> resultMap = fileService.storeImageGUI(path, projectText, file);
        if (resultMap != null) {
            tfInfo.setVisible(true);
            String project = (String) resultMap.get("project");
            String fileName = (String) resultMap.get("filename");
            tfInfo.setText("http://localhost:" + port + webBasePath + "/" + project + "/" + fileName);
        }
    }

    /**
     * 未选择文件就点击上传的错误弹窗
     *
     * @param projectText 项目名称
     * @return 是否显示弹窗
     */
    private boolean emptyAlert(String projectText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-border-color: linear-gradient(from 0.0% 0.0% to 100.0% 0.0%, #71defaff 0.0%, #fcd296ff 100.0%); -fx-border-width: 3;");
        alert.initStyle(StageStyle.UNDECORATED);
        Window window = tfInfo.getScene().getWindow();
        alert.initOwner(window);
        alert.setHeaderText("错误");
        if ("".equals(projectText.trim())) {
            alert.setContentText("项目名不能为空");
            alert.show();
            return true;
        }
        if (file == null) {
            alert.setContentText("图片不能为空");
            alert.show();
            return true;
        }
        return false;
    }

    /**
     * 上传路径选择
     */
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
     * 点击选择图片
     */
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

    /**
     * 控制图片的显示区域
     *
     * @param url 图片路径
     */
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

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
