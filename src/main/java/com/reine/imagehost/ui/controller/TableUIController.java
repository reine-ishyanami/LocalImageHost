package com.reine.imagehost.ui.controller;

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
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    }

    @FXML
    void search(ActionEvent event) {

    }

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        projectColumn.setCellValueFactory(new PropertyValueFactory<>("project"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        List<ImageWithUrl> imageWithUrls = fileService.listImage(null);
        List<SimpleImageProperty> list = imageWithUrls.stream().map((imageWithUrl) -> {
            SimpleImageProperty imageProperty = new SimpleImageProperty();
            imageProperty.setId(imageWithUrl.getId());
            imageProperty.setProject(imageWithUrl.getProject());
            imageProperty.setName(imageWithUrl.getName());
            imageProperty.setPath(imageWithUrl.getPath());
            return imageProperty;
        }).toList();
        ObservableList<SimpleImageProperty> observableList = FXCollections.observableList(list);
        tbImageList.getItems().addAll(observableList);
    }

}
