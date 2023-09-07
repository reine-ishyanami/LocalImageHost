package com.reine.imagehost.ui.controller;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.entity.ImageWithUrl;
import com.reine.imagehost.entity.SimpleImageProperty;
import com.reine.imagehost.service.FileService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TableUIController {

    @FXML
    private TableView<SimpleImageProperty> tbImageList;

    @FXML
    private TableColumn<SimpleImageProperty, SimpleIntegerProperty> idColumn;

    @FXML
    private TableColumn<SimpleImageProperty, SimpleStringProperty> projectColumn;

    @FXML
    private TableColumn<SimpleImageProperty, SimpleStringProperty> nameColumn;

    @FXML
    private TableColumn<SimpleImageProperty, SimpleStringProperty> pathColumn;

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

    private void updateTableItem(List<? extends Image> images){
        List<SimpleImageProperty> list = images.stream().map((image) -> {
            SimpleImageProperty imageProperty = new SimpleImageProperty();
            BeanUtils.copyProperties(image, imageProperty);
            return imageProperty;
        }).toList();
        ObservableList<SimpleImageProperty> observableList = FXCollections.observableList(list);
        tbImageList.getItems().clear();
        tbImageList.getItems().addAll(observableList);
    }

}
