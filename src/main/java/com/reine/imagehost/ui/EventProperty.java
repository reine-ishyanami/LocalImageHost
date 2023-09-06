package com.reine.imagehost.ui;

import javafx.stage.Stage;

import java.net.URL;

/**
 * @author reine
 */
public record EventProperty(Stage stage, URL fxmlUrl, URL iconUrl, URL cssUrl, String title) {
}
