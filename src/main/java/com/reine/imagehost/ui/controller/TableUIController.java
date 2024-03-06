package com.reine.imagehost.ui.controller;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.entity.ImageWithUrl;
import com.reine.imagehost.service.FileService;
import com.reine.imagehost.ui.component.DelButton;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TableUIController {

    @FXML
    private Label lbNotice;

    @FXML
    private TableView<Image> tbImageList;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfProject;

    private final FileService fileService;

    public TableUIController(@Qualifier("fileServiceGui") FileService fileService) {
        this.fileService = fileService;
    }


    @FXML
    void clear(ActionEvent event) {
        tfId.clear();
        tfName.clear();
        tfProject.clear();
    }

    @FXML
    void search(ActionEvent event) {
        String id = tfId.getText();
        String project = tfProject.getText();
        String name = tfName.getText();
        List<Image> images = fileService.queryImageList(id, project, name);
        updateTableItem(images);
    }

    private final FadeTransition ft = new FadeTransition();

    @FXML
    private TableColumn<Image, Void> operation;

    @FXML
    void initialize() {
        // 限制id框输入
        tfId.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.isAdded() && !change.getText().matches("\\d")) return null;
            else return change;
        }));
        List<ImageWithUrl> imageWithUrls = fileService.listImage(null);
        updateTableItem(imageWithUrls);
        ft.setDuration(Duration.seconds(4));
        ft.setNode(lbNotice);
        ft.setFromValue(1);
        ft.setToValue(0);

        tbImageList.setColumnResizePolicy(param -> true);

        operation.setCellFactory(column -> new DelButton(fileService));
    }

    private void updateTableItem(List<? extends Image> images) {
        tbImageList.getItems().clear();
        tbImageList.getItems().addAll(images);
    }

    /**
     * 删除具体某项
     *
     * @param keyEvent
     */
    @FXML
    void removeColumnByDeleteKey(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            Image selectedItem = tbImageList.getSelectionModel().getSelectedItem();
            Optional.ofNullable(selectedItem).ifPresentOrElse(image -> {
                fileService.deleteImage(image.getProject(), image.getName());
                updateTableItem(fileService.listImage(null));
            }, () -> {
                lbNotice.setStyle("-fx-background-color: #ff6666;");
                lbNotice.setText("请先选中表格项再进行删除操作");
                ft.playFromStart();
            });
        }
    }

    @Value("${server.port}")
    private Integer port;

    @Value("${web.base.path.image}")
    private String webBasePath;

    /**
     * 双击复制web访问链接
     *
     * @param event
     */
    @FXML
    void copyUrlByDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)) {
            Image selectedItem = tbImageList.getSelectionModel().getSelectedItem();
            Optional.ofNullable(selectedItem).ifPresent(image -> {
                String url = String.format("http://localhost:%d/%s/%s/%s", port, webBasePath, image.getProject(), image.getName());
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(url);
                systemClipboard.setContent(clipboardContent);
                lbNotice.setStyle("-fx-background-color: #99cc66;");
                lbNotice.setText("已复制图片访问链接到剪贴板");
                ft.playFromStart();
            });
        }
    }
}
