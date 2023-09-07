package com.reine.imagehost.ui.controller;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.entity.ImageWithUrl;
import com.reine.imagehost.service.FileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TableUIController {

    @FXML
    private Label lbNotice;

    @FXML
    private TableView<Image> tbImageList;

    @FXML
    private TableColumn<Image, Integer> idColumn;

    @FXML
    private TableColumn<Image, String> projectColumn;

    @FXML
    private TableColumn<Image, String> nameColumn;

    @FXML
    private TableColumn<Image, String> pathColumn;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfProject;

    private final FileService fileService;

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

    @FXML
    void initialize() {
        // 限制id框输入
        tfId.setTextFormatter(new TextFormatter<String>(change -> {
            if (change.isAdded() && !change.getText().matches("\\d")) return null;
            else return change;
        }));
        List<ImageWithUrl> imageWithUrls = fileService.listImage(null);
        updateTableItem(imageWithUrls);
    }

    private void updateTableItem(List<? extends Image> images) {
        resetNotice();
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
        resetNotice();
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            Image selectedItem = tbImageList.getSelectionModel().getSelectedItem();
            Optional.ofNullable(selectedItem).ifPresentOrElse(image -> {
                fileService.deleteImage(image.getProject(), image.getName());
                updateTableItem(fileService.listImage(null));
            }, () -> {
                lbNotice.setStyle("-fx-background-color: red;");
                lbNotice.setText("请先选中表格项再进行删除操作");
            });
        }
    }

    private void resetNotice(){
        lbNotice.setStyle("-fx-background-color: #00000000;");
        lbNotice.setText("");
    }
}
