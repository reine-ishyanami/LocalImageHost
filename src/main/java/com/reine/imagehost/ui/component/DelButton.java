package com.reine.imagehost.ui.component;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.service.FileService;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.antdesignicons.AntDesignIconsOutlined;
import org.kordamp.ikonli.javafx.FontIcon;

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
            FontIcon imageView = new FontIcon(AntDesignIconsOutlined.DELETE);
            imageView.setPickOnBounds(true);
            setGraphic(imageView);
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
