package com.reine.imagehost;

import com.reine.imagehost.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LocalImageHostApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(LocalImageHostApplication.class, MainView.class, args);
    }

}
