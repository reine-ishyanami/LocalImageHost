package com.reine.imagehost.ui.component;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.service.FileService;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * @author reine
 */
public class DelButton extends TableCell<Image, Void> {
    private final FileService fileService;

    public DelButton(FileService fileService) {
        super();
        this.fileService = fileService;
    }

    @Override
    protected void updateItem(Void unused, boolean empty) {
        super.updateItem(unused, empty);
        if (empty) {
            setGraphic(null);
        } else {
            BorderPane graphic = new BorderPane();
            javafx.scene.image.ImageView imageView = new ImageView(
                    new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResource("/image/del.png")).toString())
            );
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPickOnBounds(true);
            graphic.setCenter(imageView);
            setGraphic(graphic);
            imageView.setOnMouseClicked(e -> {
                ObservableList<Image> items = getTableView().getItems();
                Image image = items.get(getIndex());
                items.remove(image);
                fileService.deleteImage(image.getProject(), image.getName());
            });
        }
    }
}
