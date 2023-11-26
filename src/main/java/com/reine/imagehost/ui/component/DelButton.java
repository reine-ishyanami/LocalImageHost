package com.reine.imagehost.ui.component;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.service.FileService;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;

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
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initStyle(StageStyle.UNDECORATED);
                DialogPane dialogPane = dialog.getDialogPane();
                dialogPane.setContentText("是否删除此图片");
                dialogPane.setStyle("-fx-border-width: 2.0; -fx-border-color: #0066cc88;");
                dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                dialog.showAndWait().ifPresent(buttonType -> {
                    if (buttonType.equals(ButtonType.OK)) {
                        ObservableList<Image> items = getTableView().getItems();
                        Image image = items.get(getIndex());
                        items.remove(image);
                        fileService.deleteImage(image.getProject(), image.getName());
                    }
                });
            });
        }
    }
}
