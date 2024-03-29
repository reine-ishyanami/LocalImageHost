package com.reine.imagehost.ui.controller;

import com.reine.imagehost.entity.Img;
import com.reine.imagehost.service.FileService;
import com.reine.imagehost.ui.EventProperty;
import com.reine.imagehost.ui.StageReadyEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Optional;

/**
 * @author reine
 * 2022/6/30 7:51
 */
@Component
@Slf4j
public class MainUIController {
    @FXML
    public TextField tfPath;

    @FXML
    public TextField tfName;

    @FXML
    public Button showBtn;

    @FXML
    private TextField tfProject;

    @FXML
    private TextField tfInfo;

    @FXML
    private ImageView ivImage;

    @FXML
    private Label label;

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
    private final FileService fileService;

    /**
     * 上传后文件的本地存储位置
     */
    @Value("${local.store}")
    private String originPath;

    /**
     * 应用启动端口
     */
    @Value("${server.port}")
    private Integer port;

    @Value("${web.base.path.image}")
    private String webBasePath;

    public MainUIController(@Qualifier("fileServiceGui") FileService fileService, ApplicationContext context) {
        this.fileService = fileService;
        this.context = context;
    }

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
        StackPane pane = (StackPane) event.getSource();
        pane.getChildren().remove(dragIntoLabel);
        label.setVisible(true);
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasUrl()) {
            String url = dragboard.getUrl();
            file = new File(url);
            setIvImage(url);
        }
    }

    @FXML
    void boxDragExited(DragEvent event) {
        StackPane pane = (StackPane) event.getSource();
        pane.getChildren().remove(dragIntoLabel);
        label.setVisible(true);
    }

    @FXML
    void boxDragEntered(DragEvent event) {
        StackPane pane = (StackPane) event.getSource();
        pane.getChildren().add(dragIntoLabel);
        label.setVisible(false);
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
        String nameText = tfName.getText();
        Img img = fileService.storeImage(path, projectText, file, nameText);
        if (img != null) {
            tfInfo.setVisible(true);
            String project = img.getProject();
            String fileName = img.getName();
            String text = String.format("http://localhost:%d/%s/%s/%s", port, webBasePath, project, fileName);
            tfInfo.setText(text);
        }
    }

    /**
     * 清空内容
     */
    @FXML
    void clearContent() {
        ivImage.setImage(null);
        label.setText("拖拽图片到此处");
        tfProject.clear();
        tfName.clear();
        tfInfo.clear();
        tfInfo.setVisible(false);
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
        if (projectText.trim().isEmpty()) {
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
     * 显示图片的最大宽度
     */
    private double ivMaxWidth;

    /**
     * 显示图片的最大高度
     */
    private double ivMaxHeight;

    /**
     * 控制图片的显示区域
     *
     * @param url 图片路径
     */
    private void setIvImage(String url) {
        Image image = new Image(url);
        ivImage.setPreserveRatio(true);
        // 设置不超过图片展示区域
        double width = image.getWidth();
        double height = image.getHeight();
        if (width > ivMaxWidth || height > ivMaxHeight) {
            ivImage.setFitWidth(400);
            ivImage.setFitHeight(250);
        } else {
            ivImage.setFitWidth(0);
            ivImage.setFitHeight(0);
        }
        ivImage.setImage(image);
        label.setText("");
    }

    private Label dragIntoLabel;

    /**
     * GUI数据初始化
     */
    @FXML
    void initialize() {
        Pane pane = (Pane) ivImage.getParent();
        this.ivMaxWidth = pane.getPrefWidth();
        this.ivMaxHeight = pane.getPrefHeight();
        if (originPath != null) {
            path = new File(originPath);
            tfPath.setText(path.getAbsolutePath());
        }

        dragIntoLabel = new Label();
        dragIntoLabel.setText("释放图片以上传");
        dragIntoLabel.setStyle("-fx-font-size: 35.0; -fx-text-fill: #0000005c;");
    }

    private final ApplicationContext context;

    /**
     * 查看图片列表
     */
    @FXML
    void showImageList() {
        Stage listStage = new Stage();
        listStage.setTitle("图片查询");
        listStage.setResizable(false);
        listStage.setOnShowing(event -> showBtn.setDisable(true));
        listStage.setOnCloseRequest(event -> showBtn.setDisable(false));
        URL fxmlUrl = getClass().getResource("/fxml/table.fxml");
        EventProperty eventProperty = new EventProperty(listStage, fxmlUrl, null, null);
        context.publishEvent(new StageReadyEvent(eventProperty));
    }
}
