package com.reine.imagehost;

import com.reine.imagehost.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class LocalImageHostApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        if (args.length != 0 && "GUI".equals(args[0])) {
            log.info("Running in GUI mode");
            launch(LocalImageHostApplication.class, MainView.class, args);
        } else {
            log.info("Running in Terminal mode");
            SpringApplication.run(LocalImageHostApplication.class, args);
        }
    }
}
