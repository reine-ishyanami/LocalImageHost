package com.reine.imagehost.ui;

import jakarta.annotation.Nullable;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author reine
 */
public record EventProperty(Stage stage, URL fxmlUrl, @Nullable URL iconUrl, @Nullable URL cssUrl,
                            @Nullable String title) {
}
